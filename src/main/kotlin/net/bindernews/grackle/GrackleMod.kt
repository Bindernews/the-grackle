package net.bindernews.grackle

import basemod.AutoAdd
import basemod.BaseMod
import basemod.abstracts.CustomSavable
import basemod.interfaces.*
import com.badlogic.gdx.graphics.Texture
import com.evacipated.cardcrawl.modthespire.Loader
import com.evacipated.cardcrawl.modthespire.ModInfo
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.metrics.Metrics
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.rooms.AbstractRoom
import net.bindernews.grackle.Events.popups
import net.bindernews.grackle.Grackle.Companion.register
import net.bindernews.grackle.api.IMultiHitManager
import net.bindernews.grackle.api.IPopup
import net.bindernews.grackle.cards.*
import net.bindernews.grackle.helper.MiscUtil
import net.bindernews.grackle.helper.addData
import net.bindernews.grackle.helper.fireheartGained
import net.bindernews.grackle.helper.sendPost
import net.bindernews.grackle.icons.registerIcons
import net.bindernews.grackle.power.BasePower
import net.bindernews.grackle.relics.BerserkerTotem
import net.bindernews.grackle.relics.LoftwingFeather
import net.bindernews.grackle.relics.SimmeringHeat
import net.bindernews.grackle.ui.CardClickableLink
import net.bindernews.grackle.ui.MainMenuMetricsRequest
import net.bindernews.grackle.variables.ExtraHitsVariable
import net.bindernews.grackle.variables.Magic2Var
import org.apache.logging.log4j.LogManager
import java.util.stream.Stream

@SpireInitializer
class GrackleMod : AddAudioSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditCardsSubscriber,
    EditStringsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, PreUpdateSubscriber,
    OnStartBattleSubscriber {

    /**
     * Various constants
     */
    object CO {
        /** Root path for image resources  */
        const val RES_IMAGES = "$MOD_RES/images"
        /** Root path of localization resources  */
        const val RES_LANG = "$MOD_RES/localization"
        const val REG_START = "begin registering {}"
        const val REG_END = "done registering {}"

        /** Metrics upload url  */
        const val METRICS_URL = "https://stats.grackle.bindernews.net"

        /** Sound effect ID */
        val SFX_QUACK = makeId("DUCK")

        /** The "Aloft" keyword  */
        val KW_ALOFT = makeId("Aloft")
    }

    override fun receiveEditCharacters() {
        log.debug(CO.REG_START, "character")
        register()
        log.debug(CO.REG_END, "character")
    }

    override fun receiveEditRelics() {
        log.debug(CO.REG_START, "relics")
        // Add shared relics
//        Stream.of(
//                new PhoenixIdol()
//        ).forEach(r -> BaseMod.addRelic(r, RelicType.SHARED));

        // Add class-specific relics
        Stream.of(
            BerserkerTotem(),
            LoftwingFeather(),
            SimmeringHeat()
        ).forEach { BaseMod.addRelicToCustomPool(it, Grackle.Co.COLOR_BLACK) }
        log.debug(CO.REG_END, "relics")
    }

    override fun receiveEditCards() {
        registerIcons()
        registerDynamicVariables()
        log.debug(CO.REG_START, "cards")
        val aa = AutoAdd(MOD_ID)
        aa.packageFilter(BaseCard::class.java)
        aa.cards()
        log.debug(CO.REG_END, "cards")
    }

    override fun receivePostInitialize() {
        registerPowers()
        popups.on(CardClickableLink.inst)
        popups.on(MainMenuMetricsRequest.inst)

        BaseMod.addSaveField<String>("$MOD_ID:version", object : CustomSavable<String> {
            override fun onSave(): String = modInfo?.ModVersion.toString() ?: ""
            override fun onLoad(p0: String?) {}
        })
    }

    override fun receivePreUpdate() {
        popups.forEach { p: IPopup -> if (p.isEnabled()) p.update() }
    }

    private fun registerPowers() {
        log.debug(CO.REG_START, "powers")
        val aa = AutoAdd(MOD_ID)
        aa.packageFilter(BasePower::class.java)
        for (c in MiscUtil.autoFindClasses(aa, AbstractPower::class.java)) {
            BaseMod.addPower(c, MiscUtil.getPowerId(c))
        }
        log.debug(CO.REG_END, "powers")
    }

    private fun registerDynamicVariables() {
        log.debug(CO.REG_START, "dynamic variables")
        BaseMod.addDynamicVariable(ExtraHitsVariable.inst)
        BaseMod.addDynamicVariable(Magic2Var.inst)
        log.debug(CO.REG_END, "dynamic variables")
    }

    override fun receiveEditStrings() {
        MiscUtil.loadLocalization(CO.RES_LANG, Settings.GameLanguage.ENG, Settings.language)
    }

    override fun receiveEditKeywords() {
        MiscUtil.loadKeywords(MOD_ID, CO.RES_LANG, Settings.GameLanguage.ENG, Settings.language)
    }

    override fun receiveAddAudio() {
        log.debug(CO.REG_START, "audio")
        val sfxPath = "$MOD_RES/audio"
        BaseMod.addAudio(CO.SFX_QUACK, "$sfxPath/duck_quack.ogg")
        log.debug(CO.REG_END, "audio")
    }

    override fun receiveOnBattleStart(room: AbstractRoom) {
        AbstractDungeon.player.fireheartGained = 0
    }

    companion object {
        val log = LogManager.getLogger(GrackleMod::class.java)
        const val MOD_ID = "grackle"
        private const val MOD_ID_COLON = "$MOD_ID:"
        const val MOD_RES = "grackleResources"
        /** The mod */
        var modInfo: ModInfo? = null

        /** Cache of loaded textures  */
        private val textureCache = HashMap<String, Texture?>()

        /**
         * Map of miscellaneous UI strings
         */
        val miscUI: Map<String, String> by lazy {
            CardCrawlGame.languagePack.getUIString("$MOD_ID:misc").TEXT_DICT
        }

        private var hasInit = false
        @Suppress("unused")
        @JvmStatic fun initialize() {
            if (!hasInit) {
                BaseMod.subscribe(GrackleMod())

                log.debug(CO.REG_START, "colors")
                Grackle.Co.registerColor()
                log.debug(CO.REG_END, "colors")

                Events.metricsRun.on { metrics: Metrics ->
                    if (metrics.type == Metrics.MetricRequestType.UPLOAD_METRICS && Grackle.isPlaying) {
                        metrics.sendPost(CO.METRICS_URL)
                    }
                }

                Events.metricsGather.add { metrics ->
                    val modVersion = modInfo?.ModVersion?.toString() ?: ""
                    metrics.addData("$MOD_ID:version", modVersion)
                    val modList = Loader.MODINFOS.map { it.ID + ":" + it.ModVersion }
                    metrics.addData("$MOD_ID:mods", modList)

                }

                // Find our mod info
                modInfo = Loader.MODINFOS.find { it.ID == MOD_ID }
                // Done
                hasInit = true
            }
        }

        /**
         * Exists so that all cards are "used", and so we have an easily-sorted list of cards.
         */
        @Suppress("unused")
        private fun makeCardList(): Array<AbstractCard> {
            return arrayOf(
                AAA(),
                AerialAce(),
                AerialAdvantage(),
                AirToGroundMissiles(),
                BePrepared(),
                BombingRun(),
                BufferInputs(),
                BurnCream(),
                BurningBird(),
                Cackle(),
                CopyCrow(),
                CrashLanding(),
                Death(),
                Defend_GK(),
                DoubleKick(),
                Duck(),
                EagleEye(),
                EmbodyFire(),
                EvasiveManeuvers(),
                FightFire(),
                FireControl(),
                FiredUpCard(),
                FireTouch(),
                FireWithin(),
                Flock(),
                FOOF(),
                Forage(),
                GentleLanding(),
                Grenenade(),
                HangarMaintenance(),
                HenPeck(),
                InFlightService(),
                MidairRefuel(),
                Murder(),
                Parachute(),
                Paratrooper(),
                PeckingOrder(),
                Perch(),
                PhoenixFeather(),
                PhoenixForm(),
                Plague(),
                ResearchAndDev(),
                RocketGrackle(),
                Scratch(),
                SelfBurn(),
                SnapGracklePop(),
                Strike_GK(),
                SummonEgrets(),
                Suplex(),
                Swoop(),
                Tailwind(),
                Takeoff(),
                TargetingComputer(),
                ThisWillHurt(),
                TryThatAgain(),
                WildFire(),
                WindowPain(),
            )
        }

        /**
         * Attempts to load and cache the texture at the given path.
         * @param path Path of the texture to load
         * @return Either the texture or `null` if not found
         */
        fun loadTexture(path: String): Texture? {
            return textureCache.computeIfAbsent(path) { path2: String ->
                try {
                    Texture(path2).apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) }
                } catch (e: Exception) {
                    log.warn(e)
                    null
                }
            }
        }

        @JvmStatic val multiHitManager: IMultiHitManager
            get() = ExtraHitsVariable.inst

        /**
         * Returns a new ID with the mod prefix.
         */
        @JvmStatic fun makeId(name: String): String {
            return MOD_ID_COLON + name
        }

        /**
         * Returns a new ID with the mod prefix and the class name as the suffix.
         */
        @JvmStatic fun makeId(clazz: Class<*>): String {
            return makeId(clazz.simpleName)
        }

        /**
         * Remove the mod prefix and colon from `id`.
         * @param id ID string
         * @return ID with prefix removed
         */
        @JvmStatic fun removePrefix(id: String): String {
            return if (id.startsWith(MOD_ID_COLON)) {
                id.substring(MOD_ID_COLON.length)
            } else {
                id
            }
        }
    }
}
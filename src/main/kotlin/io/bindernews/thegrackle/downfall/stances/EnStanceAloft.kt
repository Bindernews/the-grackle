package io.bindernews.thegrackle.downfall.stances

import io.bindernews.thegrackle.stance.StanceAloft

class EnStanceAloft : EnDelegatingStance() {
    override val inner: StanceAloft = StanceAloft()

    init {
        ID = inner.ID
        name = inner.name
        updateDescription()
    }

    companion object {
        @JvmField val STANCE_ID = StanceAloft.STANCE_ID
    }
}
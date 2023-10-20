package net.bindernews.sts;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.metrics.Metrics;

public interface GatherMetricsDataSubscriber extends ISubscriber {
    void receiveGatherMetricsData(Metrics metrics);
}

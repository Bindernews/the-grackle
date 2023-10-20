package net.bindernews.sts;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.metrics.Metrics;

public interface SendMetricsSubscriber extends ISubscriber {
    void receiveSendMetrics(Metrics metrics);
}

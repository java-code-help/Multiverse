package org.multiverse.stms.gamma.benchmarks;

import org.benchy.BenchyUtils;
import org.junit.Before;
import org.junit.Test;
import org.multiverse.stms.gamma.GammaConstants;
import org.multiverse.stms.gamma.GammaStm;
import org.multiverse.stms.gamma.transactionalobjects.GammaTxnLong;
import org.multiverse.stms.gamma.transactions.GammaTxnConfig;
import org.multiverse.stms.gamma.transactions.fat.FatFixedLengthGammaTxn;

public class ArrayUpdateDriver implements GammaConstants {

    private GammaStm stm;
    private int refCount = 1;

    @Before
    public void setUp() {
        stm = new GammaStm();
    }

    @Test
    public void test() {
        final long txCount = 1000 * 1000 * 1000;

        FatFixedLengthGammaTxn tx = new FatFixedLengthGammaTxn(new GammaTxnConfig(stm));
        GammaTxnLong[] refs = new GammaTxnLong[refCount];
        for (int k = 0; k < refs.length; k++) {
            refs[k] = new GammaTxnLong(stm, 0);
        }


        long startMs = System.currentTimeMillis();
        for (long k = 0; k < txCount; k++) {
            for (int l = 0; l < refs.length; l++) {
                refs[l].openForWrite(tx, LOCKMODE_NONE).long_value++;
            }
            tx.commit();
            tx.hardReset();
        }
        long durationMs = System.currentTimeMillis() - startMs;

        String s = BenchyUtils.operationsPerSecondPerThreadAsString(txCount, durationMs, 1);

        System.out.printf("Performance is %s transactions/second/thread\n", s);

        //assertEquals(txCount, ref.volatile_value);
        //assertEquals(txCount+initialVersion, ref.version);
    }
}

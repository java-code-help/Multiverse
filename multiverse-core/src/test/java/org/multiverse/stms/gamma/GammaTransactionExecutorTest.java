package org.multiverse.stms.gamma;

import org.junit.Before;
import org.junit.Test;
import org.multiverse.api.TransactionExecutor;
import org.multiverse.api.Transaction;
import org.multiverse.api.closures.AtomicClosure;
import org.multiverse.api.closures.AtomicIntClosure;
import org.multiverse.api.closures.AtomicLongClosure;
import org.multiverse.api.closures.AtomicVoidClosure;

import static org.junit.Assert.assertEquals;
import static org.multiverse.api.ThreadLocalTransaction.clearThreadLocalTransaction;

public class GammaTransactionExecutorTest {

    private GammaStm stm;
    private TransactionExecutor block;

    @Before
    public void setUp() {
        clearThreadLocalTransaction();
        stm = new GammaStm();
        block = stm.newTransactionFactoryBuilder()
                .newTransactionExecutor();
    }

    @Test
    public void whenAtomicIntClosureUsed() {
        int result = block.atomic(new AtomicIntClosure() {
            @Override
            public int execute(Transaction tx) throws Exception {
                return 10;
            }
        });

        assertEquals(10, result);
    }

    @Test
    public void whenAtomicLongClosureUsed() {
        long result = block.atomic(new AtomicLongClosure() {
            @Override
            public long execute(Transaction tx) throws Exception {
                return 10;
            }
        });

        assertEquals(10, result);
    }

    @Test
    public void whenAtomicVoidClosureUsed() {
        block.atomic(new AtomicVoidClosure() {
            @Override
            public void execute(Transaction tx) throws Exception {
            }
        });
    }

    @Test
    public void whenAtomicClosureUsed() {
        String result = block.atomic(new AtomicClosure<String>() {
            @Override
            public String execute(Transaction tx) throws Exception {
                return "foo";
            }
        });

        assertEquals("foo", result);
    }
}
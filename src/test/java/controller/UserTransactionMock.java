package controller;

import javax.transaction.*;

public abstract class UserTransactionMock implements UserTransaction {

    private int nest = 0;

    @Override
    public void begin() throws NotSupportedException, SystemException {
        nest++;
    }
    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        nest--;
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {

    }

    @Override
    public int getStatus() throws SystemException {
        return nest;
    }

    @Override
    public void setTransactionTimeout(int i) throws SystemException {

    }
}

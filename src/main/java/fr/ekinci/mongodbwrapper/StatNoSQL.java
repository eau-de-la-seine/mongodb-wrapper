package fr.ekinci.mongodbwrapper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of StatNoSQLMXBean
 * 
 * @author Gokan EKINCI
 */
class StatNoSQL implements StatNoSQLMXBean {
    private static AtomicInteger counterInsertQuery = new AtomicInteger(0);
    private static AtomicInteger counterSelectQuery = new AtomicInteger(0);
    private static AtomicInteger counterDeleteQuery = new AtomicInteger(0);
    
    @Override
    public int getCounterInsertQuery() {
        return counterInsertQuery.get();
    }
    
    @Override
    public int getCounterSelectQuery() {
        return counterSelectQuery.get();
    }
    
    @Override
    public int getCounterDeleteQuery() {
        return counterDeleteQuery.get();
    }
    
    @Override
    public void incrementCounterInsertQuery() {
        counterInsertQuery.incrementAndGet();
    }
    
    @Override
    public void incrementCounterSelectQuery() {
        counterSelectQuery.incrementAndGet();
    }
    
    @Override
    public void incrementCounterDeleteQuery() {
        counterDeleteQuery.incrementAndGet();
    }
    
    @Override
    public void decrementCounterInsertQuery() {
        counterInsertQuery.decrementAndGet();
    }
    
    @Override
    public void decrementCounterSelectQuery() {
        counterSelectQuery.decrementAndGet();
    }
    
    @Override
    public void decrementCounterDeleteQuery() {
        counterDeleteQuery.decrementAndGet();
    }
}

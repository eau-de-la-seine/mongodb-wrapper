package fr.ekinci.mongodbwrapper;


/**
 * An MXBean interface for StatNoSQL
 * 
 * @author Gokan EKINCI
 */
public interface StatNoSQLMXBean {
    public int getCounterInsertQuery();
    public int getCounterSelectQuery();
    public int getCounterDeleteQuery();
    public void incrementCounterInsertQuery();
    public void incrementCounterSelectQuery();
    public void incrementCounterDeleteQuery();
    public void decrementCounterInsertQuery();
    public void decrementCounterSelectQuery();
    public void decrementCounterDeleteQuery();
}

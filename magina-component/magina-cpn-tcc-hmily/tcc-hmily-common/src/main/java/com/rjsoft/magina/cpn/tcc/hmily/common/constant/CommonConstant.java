package com.rjsoft.magina.cpn.tcc.hmily.common.constant;

/**
 * CommonConstant.
 *
 * @author xiaoyu
 */
public final class CommonConstant {

    private CommonConstant() {
    }

    /**
     * The constant DB_MYSQL.
     */
    public final static String DB_MYSQL = "mysql";

    /**
     * The constant DB_SQLSERVER.
     */
    public final static String DB_SQLSERVER = "sqlserver";

    /**
     * The constant DB_ORACLE.
     */
    public final static String DB_ORACLE = "oracle";

    /**
     * The constant DB_POSTGRESQL.
     */
    public final static String DB_POSTGRESQL = "postgresql";

    /**
     * The constant PATH_SUFFIX.
     */
    public final static String PATH_SUFFIX = "/tcc";

    /**
     * The constant DB_SUFFIX.
     */
    public final static String DB_SUFFIX = "hmily_";

    /**
     * The constant RECOVER_REDIS_KEY_PRE.
     */
    public final static String RECOVER_REDIS_KEY_PRE = "hmily:transaction:%s";

    /**
     * The constant HMILY_TRANSACTION_CONTEXT.
     */
    public final static String HMILY_TRANSACTION_CONTEXT = "HMILY_TRANSACTION_CONTEXT";

    /**
     * The constant LINE_SEPARATOR.
     */
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

}

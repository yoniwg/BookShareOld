package hgyw.com.bookshare.backend;

/**
 * Created by Yoni on 3/17/2016.
 */
public class BackendFactory {

    private enum DatabaseType { LISTS, SQL_LITE ,MY_SQL}

    private static DatabaseType currentDB = DatabaseType.LISTS;

    private static Backend backend;


    /**
     * @return
     */
    static public Backend getInstance(){
        if (backend == null) {
            switch (currentDB) {
                case LISTS:
                    backend = ListsBackendImpl.INSTANCE;
                    break;
                case SQL_LITE:
                    //TODO
                    break;
                case MY_SQL:
                    //TODO
                    break;
            }
        }
        return backend;
    }
}

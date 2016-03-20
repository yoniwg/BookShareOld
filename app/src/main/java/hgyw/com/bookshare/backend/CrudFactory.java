package hgyw.com.bookshare.backend;

/**
 * Created by Yoni on 3/17/2016.
 */
public class CrudFactory {

    private enum DatabaseType { LISTS, SQL_LITE ,MY_SQL}

    private static DatabaseType currentDB = DatabaseType.LISTS;

    private static Crud crud;


    /**
     * @return
     */
    static public Crud getInstance(){
        if (crud == null) {
            switch (currentDB) {
                case LISTS:
                    crud = ListsCrudImpl.INSTANCE;
                    break;
                case SQL_LITE:
                    //TODO
                    break;
                case MY_SQL:
                    //TODO
                    break;
            }
        }
        return crud;
    }
}

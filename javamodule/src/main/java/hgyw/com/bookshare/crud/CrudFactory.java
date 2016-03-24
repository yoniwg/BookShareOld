package hgyw.com.bookshare.crud;

/**
 * Created by Yoni on 3/17/2016.
 */
public class CrudFactory {

    private enum DatabaseType { LISTS, SQL_LITE ,MY_SQL}

    private static DatabaseType currentDB = DatabaseType.LISTS;

    private static ExpandedCrud crud;

    private CrudFactory() {}

    /**
     * @return
     */
    static synchronized public ExpandedCrud getInstance(){
        if (crud == null) {
            switch (currentDB) {
                case LISTS:
                    crud = new ExpandedCrudImpl();
                    new CrudTest(crud).addData();
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

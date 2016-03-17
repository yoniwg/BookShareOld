package hgyw.com.bookshare.model;

/**
 * Created by Yoni on 3/13/2016.
 */
public class AccessManager {
    private long id;
    public AccessManager(Registration rg){
        id = rg.getID();
    }

    GeneralAccess generalAccess;
    CustomerAccess customerAccess;
    SupplierAccess supplierAccess;

    void require
    GeneralAccess getGeneralAccess() {}
    CustomerAccess getCustomerAccess();
    SupplierAccess getSupplierAccess();
}

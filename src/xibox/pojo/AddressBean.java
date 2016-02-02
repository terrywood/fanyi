package xibox.pojo;

/**
 * Created by Administrator on 14-2-26.
 */
public class AddressBean {

    private String shippingEn;
    private String shippingCn ;
    private String zipCode;
    private String postCode ;

    public String getShippingCn() {
        return shippingCn;
    }

    public void setShippingCn(String shippingCn) {
        this.shippingCn = shippingCn;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    private String address1 ;
    private String address2 ;
    private String address3 ;
    private String nameEn;

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getShippingEn() {
        return shippingEn;
    }

    public void setShippingEn(String shippingEn) {
        this.shippingEn = shippingEn;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }


}

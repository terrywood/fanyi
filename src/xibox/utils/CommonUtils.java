package xibox.utils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xibox.pojo.AddressBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 14-3-3.
 */
public class CommonUtils {
    private static String[] municipality = new String[]{"北京","上海","天津","重庆"};
    private static Pattern pattern = Pattern.compile("\\([0-9]{6}\\)");

    public  static AddressBean parseAddress(Object obj){
        AddressBean bean = new AddressBean();
        String address = StringUtils.trim(obj.toString());
        String zipCode = null; ;
        //address = address.replaceAll("\\([0-9]{6}\\)","");
        Matcher matcher = pattern.matcher(address);
        if (matcher.find()) {
            zipCode = matcher.group();
            address = StringUtils.removeEnd(address,zipCode);
            //System.out.println("----" + ": '" + matcher.group() + "'");
        }
        if(StringUtils.startsWithAny(address, municipality)){
            address = StringUtils.replace(address, " ", "\n",2);
            if(StringUtils.substring(address,2,3).equals("市")){
                address = StringUtils.replaceOnce(address,"市","");
            }
        }else{
            address = StringUtils.replace(address, " ", "\n", 3);
            address = StringUtils.replaceOnce(address, "\n", ",");
        }
        bean.setShippingCn(address);
        bean.setZipCode(zipCode);
        return bean;
    }

    public  static AddressBean parseAddressEn(JSONObject json) throws JSONException {
        AddressBean obj = new AddressBean();
        String address = "";
        if(json!=null){
            JSONArray array = json.getJSONArray("trans_result");
            String postCode = array.getJSONObject(0).getString("dst");
           //String address1  = null;
            if(array.length()==3){
                address = array.getJSONObject(1).getString("dst");
                String[] temp = address.split(" ");
                int tag = temp.length/3;
                obj.setAddress1(StringUtils.join(temp, " ", 0, tag));
                obj.setAddress2(StringUtils.join(temp, " ",tag, tag*2));
                obj.setAddress3(StringUtils.join(temp, " ",tag*2, temp.length));

            }else if(array.length()==4){
                String address1 = array.getJSONObject(1).getString("dst");
                obj.setAddress1(address1);
                String[] temp = array.getJSONObject(2).getString("dst").split(" ");
                int tag = temp.length/2;
                obj.setAddress2(StringUtils.join(temp, " ", 0, tag));
                obj.setAddress3(StringUtils.join(temp, " ",tag, temp.length));
            }

            obj.setNameEn(array.getJSONObject(array.length()-1).getString("dst"));
            obj.setPostCode(postCode);
            obj.setShippingEn((StringUtils.join(new String[]{obj.getPostCode(), obj.getAddress1(), obj.getAddress2(), obj.getAddress3()}, " ")));
            System.out.println(obj.getShippingEn());
            System.out.println(obj.getPostCode());
            System.out.println(obj.getAddress1());
            System.out.println(obj.getAddress2());
            System.out.println(obj.getAddress3());
            System.out.println(obj.getNameEn());
            System.out.println(obj.getNameEn());
        }

        return obj;
    }
}

package com.paulyung.pyphoto.bean;

import java.util.List;

/**
 * Created by yang on 2016/12/5.
 * paulyung@outlook.com
 */

public class Location {
    private int status;
    private String info;
    private List<Regeocodes> regeocodes;

    public static class Regeocodes {
        private String formatted_address;//地址信息 省+市+区+乡镇+街道+门牌号
        private Element addressComponent;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public Element getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(Element addressComponent) {
            this.addressComponent = addressComponent;
        }

        public static class Element {
            private String province;//省
            private String city;//市
            private String district;//区
            private String township;//乡镇/街道

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getTownship() {
                return township;
            }

            public void setTownship(String township) {
                this.township = township;
            }
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Regeocodes> getRegeocodes() {
        return regeocodes;
    }

    public void setRegeocodes(List<Regeocodes> regeocodes) {
        this.regeocodes = regeocodes;
    }
}

package archenoah.lib.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import archenoah.lib.custom.AddressHolder.AddressEntry;

public class AddressHolder extends ArrayList<AddressEntry>{
    
    //private  holder = new ArrayList<AddressHolder.AddressEntry>();

    public AddressHolder() {
        super();
    }

    public Integer getLength() {
        return this.size();
    }

    public double getLat(Integer index) {
        return get(index).getLat();
    }

    public double getLon(Integer index) {
        return get(index).getLon();
    }

    public String getCaption(Integer index) {
        return get(index).generateCaption();
    }

    public String[] toAddressArray() {

        String[] result = new String[this.size()];
        Integer index = 0;

        for (AddressEntry address : this) {

            String entry = "";

            if (address.street != null) {
                entry += address.getStreet() + ", ";
            }

            if (address.postalCode != null) {
                entry += address.getPostalCode();

                if (address.city == null) {
                    entry += ", ";
                } else {
                    entry += " ";
                }
            }

            if (address.city != null) {
                entry += address.getCity() + ", ";
            }

            if (address.countryCode != null) {
                entry += address.getCountryCode();
            }

            result[index] = entry;

            index++;
        }

        return result;

    }

    public HashMap<String, String[]> toPostalCodeMap() {
        HashMap<String, String[]> result = new HashMap<String, String[]>();

        for (AddressEntry address : this) {

            if (address.getPostalCode() != null) {
                result.put(address.getPostalCode(), new String[2]);
            }

        }

        return result;

    }

    public String toPostalCodeString() {

        HashMap<String, String[]> postalcodes = toPostalCodeMap();

        Boolean first = true;
        String result = "";
        for (Entry<String, String[]> entry : postalcodes.entrySet()) {
            if (!first) {
                result += ",";
            } else {
                first = false;
            }
            result += entry.getKey();

        }

        return result;
    }

    public static AddressEntry newAddress(String identifier) {
        return new AddressEntry(identifier);
    }

    public static AddressEntry newAddress(String identifier, String countryCode, String postalCode, String city, String street) {
        return new AddressEntry(identifier, countryCode, postalCode, city, street);
    }

    public static AddressEntry newAddress(String identifier, String countryCode, String postalCode, String city, String street, double lat, double lon) {
        return new AddressEntry(identifier, countryCode, postalCode, city, street, lat, lon);
    }

    public static AddressEntry newAddress(String identifier, String countryCode, String postalCode, String city, String street, java.util.HashMap<String, Double> ende) {
        return new AddressEntry(identifier, countryCode, postalCode, city, street, ende);
    }

    /****/

    public static class AddressEntry {

        private String countryCode;
        private String postalCode;
        private String city;
        private String street;

        private String identifier;
        private double lat;
        private double lon;

        public AddressEntry(String identifier) {
            this.identifier = identifier;
        }

        public AddressEntry(String identifier, String countryCode, String postalCode, String city, String street) {
            this(identifier);
            this.countryCode = countryCode;
            this.postalCode = postalCode;
            this.city = city;
            this.street = street;

        }

        public AddressEntry(String identifier, String countryCode, String postalCode, String city, String street, double lat, double lon) {
            this(identifier, countryCode, postalCode, city, street);
            this.lat = lat;
            this.lon = lon;
        }

        public AddressEntry(String identifier, String countryCode, String postalCode, String city, String street, java.util.HashMap<String, Double> point) {
            this(identifier, countryCode, postalCode, city, street);
            if(point != null && point.get("lat") != null && point.get("lon") != null) {
                this.lat = point.get("lat");
                this.lon = point.get("lon");
            }
        }

        public String generateCaption() {
            return this.identifier + " (" + this.postalCode + " " + this.city + ")";
        }

        /* getters and setters */

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double latitude) {
            this.lat = latitude;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double longitude) {
            this.lon = longitude;
        }

        public void setLatLon(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public void setLatLon(java.util.HashMap<String, Double> point) {
            this.lat = point.get("lat");
            this.lon = point.get("lon");
        }
    }

}

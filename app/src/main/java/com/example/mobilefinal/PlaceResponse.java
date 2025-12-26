package com.example.mobilefinal;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlaceResponse {
    @SerializedName("type")
    private String type;

    @SerializedName("features")
    private List<Feature> features;

    public String getType() {
        return type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public static class Feature {
        @SerializedName("type")
        private String type;

        @SerializedName("properties")
        private Properties properties;

        @SerializedName("geometry")
        private Geometry geometry;

        public String getType() {
            return type;
        }

        public Properties getProperties() {
            return properties;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public static class Properties {
            @SerializedName("name")
            private String name;

            @SerializedName("country")
            private String country;

            @SerializedName("country_code")
            private String countryCode;

            @SerializedName("state")
            private String state;

            @SerializedName("city")
            private String city;

            @SerializedName("postcode")
            private String postcode;

            @SerializedName("street")
            private String street;

            @SerializedName("housenumber")
            private String houseNumber;

            @SerializedName("lon")
            private double lon;

            @SerializedName("lat")
            private double lat;

            @SerializedName("formatted")
            private String formatted;

            @SerializedName("address_line1")
            private String addressLine1;

            @SerializedName("address_line2")
            private String addressLine2;

            public String getName() {
                return name;
            }

            public String getCountry() {
                return country;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public String getState() {
                return state;
            }

            public String getCity() {
                return city;
            }

            public String getPostcode() {
                return postcode;
            }

            public String getStreet() {
                return street;
            }

            public String getHouseNumber() {
                return houseNumber;
            }

            public double getLon() {
                return lon;
            }

            public double getLat() {
                return lat;
            }

            public String getFormatted() {
                return formatted;
            }

            public String getAddressLine1() {
                return addressLine1;
            }

            public String getAddressLine2() {
                return addressLine2;
            }
        }

        public static class Geometry {
            @SerializedName("type")
            private String type;

            @SerializedName("coordinates")
            private List<Double> coordinates;

            public String getType() {
                return type;
            }

            public List<Double> getCoordinates() {
                return coordinates;
            }
        }
    }
}

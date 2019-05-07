package ehi.message;

import ehi.gps.classifier.Scheme;
import ehi.gps.model.Country;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    public Scheme scheme;

    public Country country;

    public List<Country> countries;

}

package nl.wesleydekraker.windmillservice;

import nl.wesleydekraker.windmillinterface.*;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class WindmillServiceImplTest {
    // Unit tests

    @Test
    public void read() throws Exception {
        try {
            checkIfWindmill1Exists();
            fail("De methode read geeft geen foutmelding als een object niet gevonden is!");
        } catch (Fault e) { }
    }

    @Test
    public void update() throws Exception {
        WindmillSuccess windmillSuccess = updateWindmill1ToWindmill2();
        assertTrue("De methode update geeft geen foutmelding als een object niet gevonden is!",
                windmillSuccess.isSuccess() == false);
    }

    @Test
    public void delete() throws Exception {
        WindmillSuccess windmillSuccess = deleteWindmill2();
        assertTrue("De methode delete geeft geen foutmelding als een object niet gevonden is!!",
                windmillSuccess.isSuccess() == false);
    }

    // Integration tests
    @Test
    public void crud() throws Exception {
        createWindmill1();
        checkIfWindmill1Exists();
        updateWindmill1ToWindmill2();
        checkIfWindmill2Exists();
        deleteWindmill2();
        checkIfWindmill2IsDeleted();
    }

    private WindmillServiceImpl getWindmillServiceImpl() {
        return new WindmillServiceImpl();
    }

    private WindmillDetails getWindmillDetails1() {
        return createWindmillDetails("GE 1.5MW",
                1.5f,
                new BigInteger("116"), new BigInteger("212"),
                52.325491f, 4.579099f);
    }

    private WindmillDetails getWindmillDetails2() {
        return createWindmillDetails("GE 1.5MW",
                1.6f,
                new BigInteger("116"), new BigInteger("212"),
                52.325491f, 4.579099f);
    }

    private WindmillIdentity getWindmillIdentity1() {
        return createWindmillIdentity(new BigInteger("1"));
    }

    private Windmill getWindmill2() {
        return createWindmill(getWindmillIdentity1(), getWindmillDetails2());
    }

    private WindmillIdentity createWindmillIdentity(BigInteger id) {
        ObjectFactory factory = new ObjectFactory();
        WindmillIdentity windmillIdentity = factory.createWindmillIdentity();
        windmillIdentity.setId(id);

        return windmillIdentity;
    }

    private WindmillDetails createWindmillDetails(String model,
                                                  float power,
                                                  BigInteger lengthBlade, BigInteger heightTower,
                                                  float latitude, float longitude) {
        ObjectFactory factory = new ObjectFactory();
        WindmillDetails windmillDetails = factory.createWindmillDetails();
        windmillDetails.setModel(model);
        windmillDetails.setPower(power);
        windmillDetails.setHeightTower(lengthBlade);
        windmillDetails.setLengthBlade(heightTower);
        windmillDetails.setLatitude(latitude);
        windmillDetails.setLongitude(longitude);

        return windmillDetails;
    }

    private Windmill createWindmill(WindmillIdentity windmillIdentity, WindmillDetails windmillDetails) {
        ObjectFactory factory = new ObjectFactory();
        Windmill windmill = factory.createWindmill();
        windmill.setIdentity(windmillIdentity);
        windmill.setDetails(windmillDetails);

        return windmill;
    }

    private boolean compareWindmillDetails(WindmillDetails details1,
                                            WindmillDetails details2) {
        return details1.getModel().equals(details2.getModel()) &&
                details1.getPower() == details2.getPower() &&
                details1.getHeightTower().compareTo(details2.getHeightTower()) == 0 &&
                details1.getLengthBlade().compareTo(details2.getLengthBlade()) == 0 &&
                details1.getLatitude() == details2.getLatitude() &&
                details1.getLongitude() == details2.getLongitude();
    }

    private WindmillIdentity createWindmill1() throws Exception {
        return getWindmillServiceImpl().create(getWindmillDetails1());
    }

    private void checkIfWindmill1Exists() throws Exception {
        assertTrue("Windmolen 1 is niet goed opslagen!",
                compareWindmillDetails(
                        getWindmillDetails1(),
                        getWindmillServiceImpl().read(getWindmillIdentity1())
                )
        );
    }

    private WindmillSuccess updateWindmill1ToWindmill2() throws Exception {
        return getWindmillServiceImpl().update(getWindmill2());
    }

    private void checkIfWindmill2Exists() throws Exception {
        assertTrue("Windmolen 1 is niet geüpdate naaar windmolen 2!",
                compareWindmillDetails(
                        getWindmillDetails2(),
                        getWindmillServiceImpl().read(getWindmillIdentity1())
                )
        );
    }

    private WindmillSuccess deleteWindmill2() throws  Exception {
        return getWindmillServiceImpl().delete(getWindmillIdentity1());
    }

    private void checkIfWindmill2IsDeleted() throws  Exception {
        try {
            getWindmillServiceImpl().read(getWindmillIdentity1());
            fail("Windmolen 2 is niet correct verwijderd!");
        } catch (Fault e) { }
    }
}

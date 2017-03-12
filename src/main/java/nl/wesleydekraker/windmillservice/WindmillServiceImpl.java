package nl.wesleydekraker.windmillservice;

import javax.jws.WebService;

import com.sun.xml.ws.developer.SchemaValidation;
import nl.wesleydekraker.windmillinterface.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "nl.wesleydekraker.windmillinterface.WindmillServiceInterface")
@SchemaValidation(handler = SchemaValidationErrorHandler.class)
public class WindmillServiceImpl implements WindmillServiceInterface {
    private static List<Windmill> allWindmills = new ArrayList<>();

    @Override
    public WindmillIdentity create(WindmillDetails windmillDetails) throws Fault {
        ObjectFactory factory = new ObjectFactory();
        Windmill newWindmill = factory.createWindmill();
        WindmillIdentity windmillIdentity = factory.createWindmillIdentity();

        newWindmill.setDetails(windmillDetails);
        newWindmill.setIdentity(windmillIdentity);

        newWindmill.getIdentity().setId(newId());

        allWindmills.add(newWindmill);

        return windmillIdentity;
    }

    @Override
    public WindmillDetails read(WindmillIdentity windmillIdentity) throws Fault {
        ObjectFactory factory = new ObjectFactory();

        int index = indexOfWindmill(windmillIdentity.getId());

        if (index != -1) {
            return allWindmills.get(index).getDetails();
        } else {
            WindmillFault windmillFault = factory.createWindmillFault();
            windmillFault.setErrorCode((short) 1);
            windmillFault.setMessage("Geen windmolen gevonden met id: " + windmillIdentity.getId() + ".");
            Fault fault = new Fault("Er ging iets mis met het ophalen van een windmolen", windmillFault);
            throw fault;
        }
    }

    @Override
    public WindmillSuccess update(Windmill windmill) throws Fault {
        ObjectFactory factory = new ObjectFactory();
        WindmillSuccess windmillSuccess = factory.createWindmillSuccess();

        int index = indexOfWindmill(windmill.getIdentity().getId());
        windmillSuccess.setSuccess(index != -1);

        if (windmillSuccess.isSuccess()) {
            allWindmills.set(index, windmill);
        }

        return windmillSuccess;
    }

    @Override
    public WindmillSuccess delete(WindmillIdentity windmillIdentity) throws Fault {
        ObjectFactory factory = new ObjectFactory();
        WindmillSuccess windmillSuccess = factory.createWindmillSuccess();

        int index = indexOfWindmill(windmillIdentity.getId());
        windmillSuccess.setSuccess(index != -1);

        if (windmillSuccess.isSuccess()) {
            allWindmills.remove(index);
        }

        return windmillSuccess;
    }

    private int indexOfWindmill(BigInteger windmillId) {
        int index = -1;
        for (int i = 0; i < allWindmills.size(); i++) {
            if (allWindmills.get(i).getIdentity().getId().compareTo(windmillId) == 0) {
                index = i;
                break;
            }
        }

        return index;
    }

    private BigInteger newId() {
        BigInteger max = new BigInteger("0");
        for (Windmill windmill : allWindmills) {
            if (max.compareTo(windmill.getIdentity().getId()) == -1) {
                max = windmill.getIdentity().getId();
            }
        }

        return max.add(new BigInteger("1"));
    }
}

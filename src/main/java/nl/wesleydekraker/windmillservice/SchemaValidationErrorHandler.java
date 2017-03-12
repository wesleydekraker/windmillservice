package nl.wesleydekraker.windmillservice;

import com.sun.xml.ws.developer.ValidationErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SchemaValidationErrorHandler extends ValidationErrorHandler {
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        handleMessage("Warning", exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        handleMessage("Error", exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        handleMessage("Fatal", exception);
    }

    private String handleMessage(String level, SAXParseException exception) throws SAXException {
        String message = "[" + level + "] line nr: " +  exception.getLineNumber() +
                " column nr: " + exception.getColumnNumber() + " message: " + exception.getMessage();

        throw new SAXException(message);
    }
}

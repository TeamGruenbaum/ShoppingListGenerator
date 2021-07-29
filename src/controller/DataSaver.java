package controller;

import java.io.IOException;
import java.io.Serializable;

public interface DataSaver
{
    <T extends Serializable> void write(T object, String target) throws IOException;
    <T extends Serializable> T read(String source) throws IOException, ClassNotFoundException;
}

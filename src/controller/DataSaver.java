package controller;

import java.io.Serializable;

public interface DataSaver
{
    <T extends Serializable> void write(T object, String target);
    <T extends Serializable> T read(String source);
}

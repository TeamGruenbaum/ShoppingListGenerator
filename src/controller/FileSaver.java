package controller;

import java.io.*;

public class FileSaver implements DataSaver
{
    private String storagePath;



    public FileSaver(String directoryNameInHome)
    {
        setStoragePath(directoryNameInHome);
    }

    private void setStoragePath(String directoryName)
    {
        storagePath=System.getProperty("user.home")+(System.getProperty("os.name").contains("Windows")?"\\"+directoryName:"/"+directoryName);
    }


    @Override
    public <T extends Serializable> void write(T object, String target) throws IOException
    {
        File targetFile=new File(storagePath, target);
        targetFile.getParentFile().mkdirs();

        write(object, new FileOutputStream(targetFile, false));
    }

    private static <T extends Serializable> void write(T object, OutputStream output) throws IOException
    {
        ObjectOutputStream objectWriter=new ObjectOutputStream(output);

        objectWriter.writeObject(object);

        objectWriter.close();
        output.close();
    }


    @Override
    public <T extends Serializable> T read(String source) throws IOException, ClassNotFoundException
    {
        return read(new FileInputStream(new File(storagePath, source)));
    }

    private  <T extends Serializable> T read(InputStream input) throws IOException, ClassNotFoundException
    {
        ObjectInputStream objectReader=new ObjectInputStream(input);

        T readObject=(T) objectReader.readObject();

        objectReader.close();
        input.close();

        return readObject;
    }
}

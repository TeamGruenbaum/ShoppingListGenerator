package controller;



public class Pair<K,V> implements PairedValue<K,V>
{
    private K key;
    private V value;



    public Pair(K key, V value)
    {
        this.key=key;
        this.value=value;
    }


    @Override
    public K getKey()
    {
        return key;
    }

    @Override
    public void setKey(K key)
    {
        this.key=key;
    }

    @Override
    public V getValue()
    {
        return value;
    }

    @Override
    public void setValue(V value)
    {
        this.value=value;
    }
}

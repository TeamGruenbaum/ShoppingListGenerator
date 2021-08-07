package controller;



public interface PairedValue<K,V>
{
    K getKey();
    void setKey(K key);
    V getValue();
    void setValue(V value);
}

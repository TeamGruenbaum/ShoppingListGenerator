package controller;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface SerializableBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {}

package fr.iut2.saeprojet.api;

public abstract class ResultatAppel<T> {
    public abstract void traiterResultat(T response);
    public abstract void traiterErreur();
}
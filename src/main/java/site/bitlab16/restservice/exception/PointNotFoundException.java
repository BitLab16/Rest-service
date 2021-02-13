package site.bitlab16.restservice.exception;

public class PointNotFoundException extends RuntimeException{
    public PointNotFoundException(Long id) {
        super("Could not find point " + id);
    }
}

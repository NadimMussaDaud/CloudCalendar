package Exceptions;

public class NonExistentAccountException extends Exception {

    private String name;

    public NonExistentAccountException() {
        super();
    }
    
    public NonExistentAccountException(String name){
        super();
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

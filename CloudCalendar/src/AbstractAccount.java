abstract class AbstractAccount implements Account {
    
    private String name ;

    public AbstractAccount(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

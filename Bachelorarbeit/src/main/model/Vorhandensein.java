package main.model;

public enum Vorhandensein {
	
	VORHANDEN("VORHANDEN"),
	NICHT_VORHANDEN("NICHT_VORHANDEN"),
	UNSICHER("UNSICHER");
	
    public static final Vorhandensein[] ALL = { VORHANDEN, NICHT_VORHANDEN, UNSICHER};

 
    
    private final String name;

    
    public static Vorhandensein forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("VORHANDEN")) {
            return Vorhandensein.VORHANDEN;
        } else if (name.toUpperCase().equals("NICHT_VORHANDEN")) {
            return Vorhandensein.NICHT_VORHANDEN;
        }else {
        	return UNSICHER;
        }
    }
    
    
    private Vorhandensein(final String name) {
        this.name = name;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
}

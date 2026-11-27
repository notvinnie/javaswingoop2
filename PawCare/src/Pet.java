public class Pet {
    private static int nextId = 1;

    public final int id;
    public final String ownerUsername;
    public String name;
    public String species;
    public String breed;
    public int age;
    public String notes;

    public Pet(String ownerUsername, String name, String species,
               String breed, int age, String notes) {
        this.id = nextId++;
        this.ownerUsername = ownerUsername;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.notes = notes;
    }
}
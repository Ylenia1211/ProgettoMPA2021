package model;

import java.util.Date;

public class Pet extends MasterData{
    public static class Builder<T extends Pet.Builder<T>>  extends MasterData.Builder<Pet.Builder<T>>{
        private String id_petRace;
        private String id_owner;
        private String particularSign;


        public Builder(String id_petRace, String id_owner, String particularSign){
            super();
            this.id_petRace = id_petRace;
            this.id_owner = id_owner;
            this.particularSign = particularSign;
        }

        public Builder() {
            super();
        }

        public Builder setId_petRace(String id_petRace) {
            this.id_petRace = id_petRace;
            return this;
        }

        public Builder setId_owner(String id_owner) {
            this.id_owner = id_owner;
            return this;
        }

        public Builder setParticularSign(String particularSign) {
            this.particularSign = particularSign;
            return this;
        }

        @Override
        public Pet.Builder getThis(){
            return  this;
        }

        public Pet build(){
            return new Pet(this);
        }
    }


    final private String id_petRace;
    final private String id_owner;
    final private String particularSign;

    protected Pet(Pet.Builder builder){
        super (builder);
        this.id_owner = builder.id_owner;
        this.id_petRace = builder.id_petRace;
        this.particularSign = builder.particularSign;
    }

    public String getId_petRace() {
        return id_petRace;
    }

    public String getId_owner() {
        return id_owner;
    }

    public String getParticularSign() {
        return particularSign;
    }
}

package am2.common.blocks.tileentity.flickers;

public enum FLICKERS {
    BUTCHERY ( 0 ),
    CONTAINMENT ( 1 ),
    FELLEDOAK ( 2 ),
    FISHING ( 3 ),
    FLATLANDS ( 4 ),
    GENTLERAINS ( 5 ),
    INTERDICTION ( 6 ),
    ITEMTRANSPORT ( 7 ),
    LIGHT ( 8 ),
    MOONSTONEATTRACTOR ( 9 ),
    NATURESBOUNTY ( 10 ),
    PACKEDEARTH ( 11 ),
    PROGENY ( 12 );

    private int id;

    FLICKERS ( int a ) {
        this.id = a;
    }

    public int getID ( ) {
        return id;
    }

}

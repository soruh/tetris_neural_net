package game_logic;

public class Block {
    private static final int[][][][] blockStates = new int[][][][] {
            // Empty
            null,
            // Block: 1
            {
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            },
            // I: 2
            {
                    {{-1, 0}, {0, 0}, {1, 0}, {2, 0}},
                    {{1, 2}, {1, 1}, {1, 0}, {1, -1}},
                    {{-1, 0}, {0, 0}, {1, 0}, {2, 0}},
                    {{1, 2}, {1, 1}, {1, 0}, {1, -1}},
            },
            // L: 3
            {
                    {{0, 0}, {1, 0}, {2, 0}, {0, -1}},
                    {{0, 1}, {0, 0}, {0, -1}, {-1, 1}},
                    {{-1, 0}, {0, 0}, {1, 0}, {1, 1}},
                    {{0, 1}, {0, 0}, {0, -1}, {1, -1}},
            },
            // LInverted: 4
            {
                    {{0, 0}, {1, 0}, {2, 0}, {2, -1}},
                    {{0, 1}, {0, 0}, {0, -1}, {-1, -1}},
                    {{-1, 0}, {0, 0}, {1, 0}, {-1, 1}},
                    {{1, 1}, {0, 1}, {0, 0}, {0, -1}},
            },
            // T: 5
            {
                    {{-1, 0}, {0, 0}, {1, 0}, {0, -1}},
                    {{0, 1}, {0, 0}, {0, -1}, {1, 0}},
                    {{0, 1}, {-1, 0}, {0, 0}, {1, 0}},
                    {{0, 1}, {0, 0}, {-1, 0}, {0, -1}},
            },
            // Step: 6
            {
                    {{0, 0}, {1, 0}, {1, -1}, {2, -1}},
                    {{0, 0}, {1, 0}, {1, 1}, {0, -1}},
                    {{0, 0}, {1, 0}, {1, -1}, {2, -1}},
                    {{0, 0}, {1, 0}, {1, 1}, {0, -1}},
            },
            // StepInverted: 7
            {
                    {{1, 0}, {2, 0}, {0, -1}, {1, -1}},
                    {{1, 0}, {2, 0}, {1, 1}, {2, -1}},
                    {{1, 0}, {2, 0}, {0, -1}, {1, -1}},
                    {{1, 0}, {2, 0}, {1, 1}, {2, -1}},
            },
    };

    public int getType() {
        return type;
    }

    private int type;
    private int rotation;
    private int[] position;


    public Block (int pType) {
        type = pType;
        rotation = 0;
        position = new int[2];
    }

    public void setPosition(int[] position){ this.position = position; }

    public void setPosition(int xPosition, int yPosition){ this.position = new int[]{xPosition, yPosition}; }

    public void setRotation(int pRotation){ this.rotation = pRotation; this.rotation = this.rotation % 4; }


    public int[] getPosition(){ return this.position; }

    public int getRotation(){ return this.rotation; }


    public int[][] getAbsoluteCells(){
        int[][] relativeCells = blockStates[type][rotation];

        int[][] absoluteCells = new int[relativeCells.length][relativeCells[0].length];
        for(int i=0; i<relativeCells.length; i++) {
            absoluteCells[i] = new int[]{relativeCells[i][1] + position[0], relativeCells[i][0] + position[1]};
        }

        return absoluteCells;
    }
}

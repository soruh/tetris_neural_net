package game_logic;


public class Block {
    private static final int[][][][] blockStates = new int[][][][] {
            // Block: 0
            {
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            },
            // I: 1
            {
                    {{-1, 0}, {0, 0}, {1, 0}, {2, 0}},
                    {{1, 2}, {1, 1}, {1, 0}, {1, -1}},
                    {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
                    {{1, 1}, {1, 0}, {1, -1}, {1, -2}},
            },
            // L: 2
            {
                    {{0, 0}, {1, 0}, {2, 0}, {0, -1}},
                    {{0, 1}, {0, 0}, {0, -1}, {-1, 1}},
                    {{-1, 0}, {0, 0}, {1, 0}, {1, 1}},
                    {{0, 1}, {0, 0}, {0, -1}, {1, -1}},
            },
            // LInverted: 3
            {
                    {{0, 0}, {1, 0}, {2, 0}, {2, -1}},
                    {{0, 1}, {0, 0}, {0, -1}, {-1, -1}},
                    {{-1, 0}, {0, 0}, {1, 0}, {-1, 1}},
                    {{1, 1}, {0, 1}, {0, 0}, {0, -1}},
            },
            // T: 4
            {
                    {{-1, 0}, {0, 0}, {1, 0}, {0, -1}},
                    {{1, 0}, {-1, 0}, {0, 0}, {1, -1}},
                    {{0, 1}, {-1, 0}, {0, 0}, {1, 0}},
                    {{0, 1}, {0, 0}, {1, 0}, {0, -1}},
            },
            // Step: 5
            {
                    {{1, 0}, {2, 0}, {0, -1}, {1, -1}},
                    {{2, 1}, {1, 0}, {2, 0}, {1, -1}},
                    {{1, 0}, {2, 0}, {0, -1}, {1, -1}},
                    {{2, 1}, {1, 0}, {2, 0}, {1, -1}},
            },
            // StepInverted: 6
            {
                    {{0, 0}, {1, 0}, {1, -1}, {2, -1}},
                    {{1, 0}, {1, -1}, {2, -1}, {2, -2}},
                    {{0, 0}, {1, 0}, {1, -1}, {2, -1}},
                    {{1, 0}, {1, -1}, {2, -1}, {2, -2}},
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
        int[][] absoluteCells = blockStates[type][rotation];
        for (int i = 0; i < absoluteCells.length; i++) {
            for (int j = 0; j < absoluteCells[0].length; j++) {
                absoluteCells[i][j] += position[j];
            }
        }
        return absoluteCells;
    }
}

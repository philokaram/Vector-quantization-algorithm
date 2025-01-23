class VectorQuantization{
    private int imageHight;
    private int imageWidth;
    private int blockHight;
    private int blockWidth;
    private int codeBookSize;
    private int numberOfBlocksInCodeBook = 0;
    private double[][][] codeBook;
//_______________________________________________________________________________________________________________________
    private double[][][] split(double[][][] oldCodeBook){
        double[][][] newCodeBook = new double[numberOfBlocksInCodeBook*2][blockWidth][blockHight];
        for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
            for(int i = 0 ; i < this.blockWidth ; i++){
                for(int j = 0 ; j < this.blockHight ; j++){
                    if(Math.floor(oldCodeBook[n][i][j]) == oldCodeBook[n][i][j] ){
                        newCodeBook[2*n][i][j] = oldCodeBook[n][i][j] - 1;
                        newCodeBook[(2*n)+1][i][j] = oldCodeBook[n][i][j] +1;
                    }
                    else{
                        newCodeBook[2*n][i][j] = Math.floor(oldCodeBook[n][i][j]);
                        newCodeBook[(2*n)+1][i][j] = Math.ceil(oldCodeBook[n][i][j]);
                    }
                }
            }
        }
        return newCodeBook;
    }
//_____________________________________________________________________________________________________________________________________
    private double distance(int[][] pixelsArray , int widthStep , int hightStep,double[][][] codeBook ,int codeBookIndex){
        double distance = 0;
        for(int i = 0; i < blockWidth && i < imageWidth; i++){
            for(int j = 0 ; j < blockHight && j < imageHight; j++){
                /*
                 _ _ _ _ _
                |         |
                |  *   *  |
                |         |
                |  *   *  |
                |_ _ _ _ _|
                */
                distance += Math.abs((pixelsArray[(i%blockWidth)+(blockWidth*widthStep)][(j%blockHight)+(blockHight*hightStep)])-(codeBook[codeBookIndex][i][j]));
            }
        }
        return distance;
    } 
//_________________________________________________________________________________________________________________________________________________
    private double[][][] LGBAlgorithm(int[][] pixelsArray,int imageHight , int imageWidth ,int blockWidth , int blockHight , int codeBookSize){
        this.imageHight = imageHight;
        this.imageWidth = imageWidth;
        this.blockHight = blockHight;
        this.blockWidth = blockWidth;
        this.codeBookSize = codeBookSize;
        double[][][] codeBook = new double[1][blockWidth][blockHight];
        double[][] initialBlock = new double[blockWidth][blockHight];
        //------------------------
        //1-create initial block
        //------------------------
        for(int i = 0; i < imageWidth ; i++){
            for(int j = 0 ; j < imageHight ; j++){
                initialBlock[i%blockWidth][j%blockHight] += pixelsArray[i][j];
            }
        }
        double blocksNumber = (imageHight*imageWidth)/(blockHight*blockWidth);
        for(int i = 0; i < blockWidth ; i++){
            for(int j = 0 ; j < blockHight ; j++){
                initialBlock[i][j] /= blocksNumber;
                codeBook[0][i][j] = initialBlock[i][j];
            }
        }
        //----------------------------
        //2-create remaining blocks
        //----------------------------
        this.numberOfBlocksInCodeBook = 1;
        int widthStep;
        int hightStep;
        int maxNumberOfWidthSteps = imageWidth/blockWidth;
        int maxNumberOfHightSteps = imageHight/blockHight;
        double[]  frequency = new double[numberOfBlocksInCodeBook];
        while (numberOfBlocksInCodeBook < codeBookSize) {
            //------------
            //2.1-split
            //------------
            codeBook = split(codeBook);
            numberOfBlocksInCodeBook *=2;
            //---------------------------------
            //2.2-set blocks to codeBookBlocks
            //---------------------------------
            double [][][] averageArray = new double[numberOfBlocksInCodeBook][blockWidth][blockHight];
            frequency = new double[numberOfBlocksInCodeBook];
            widthStep = 0;
            while (widthStep < maxNumberOfWidthSteps) {
                hightStep = 0;
                while (hightStep < maxNumberOfHightSteps) {
                    /* 
                     _ _ _
                    |_|   |
                    |     |
                    |_ _ _|
                    */
                    //2.2.1-calculate min distance
                    double minDistance=0;
                    int minBlockDistanceIndex = 0;
                    for(int i = 0 ; i < numberOfBlocksInCodeBook ; i++ ){
                        double distance = distance(pixelsArray,widthStep,hightStep,codeBook,i);
                        if(i == 0){
                            minDistance = distance;
                        }
                        else if(distance < minDistance){
                            minDistance = distance;
                            minBlockDistanceIndex = i;
                        }
                    }
                    //2.2.2-add this block to average array
                    for(int i = 0; i < blockWidth ; i++){
                        for(int j = 0 ; j < blockHight ; j++){
                            averageArray[minBlockDistanceIndex][i][j] +=pixelsArray[(i%blockWidth)+(blockWidth*widthStep)][(j%blockHight)+(blockHight*hightStep)] ;
                        }
                    }
                    frequency[minBlockDistanceIndex]++;
                    hightStep ++;
                }
                widthStep ++;
            }
            for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
                for(int i = 0 ; i < this.blockWidth ; i++){
                    for(int j = 0 ; j < this.blockHight ; j++){
                        averageArray[n][i][j] /= frequency[n];
                    }
                }
            }
            codeBook = averageArray;
        }
        //---------------------------
        //3-check if there changes
        //---------------------------
        boolean thereAreChanges = true;
        while (thereAreChanges) { 
            // System.out.println("///////////");
            thereAreChanges = false;
            double [][][] averageArray = new double[numberOfBlocksInCodeBook][blockWidth][blockHight];
            double[]  newFrequency = new double[numberOfBlocksInCodeBook];
            widthStep = 0;
            hightStep = 0;
            while (widthStep < maxNumberOfWidthSteps) {
                hightStep = 0;
                while (hightStep < maxNumberOfHightSteps) {
                    double minDistance=0;
                    int minBlockDistanceIndex = 0;
                    for(int i = 0 ; i < numberOfBlocksInCodeBook ; i++ ){
                        double distance = distance(pixelsArray,widthStep,hightStep,codeBook,i);
                        if(i == 0){
                            minDistance = distance;
                        }
                        else if(distance < minDistance){
                            minDistance = distance;
                            minBlockDistanceIndex = i;
                        }
                    }
                    for(int i = 0; i < blockWidth ; i++){
                        for(int j = 0 ; j < blockHight ; j++){
                            averageArray[minBlockDistanceIndex][i][j] +=pixelsArray[(i%blockWidth)+(blockWidth*widthStep)][(j%blockHight)+(blockHight*hightStep)] ;
                        }
                    }
                    newFrequency[minBlockDistanceIndex]++;
                    hightStep ++;
                }
                widthStep ++;
            }
            for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
                if(newFrequency[n] != frequency[n]){
                    thereAreChanges = true;
                }
                for(int i = 0 ; i < this.blockWidth ; i++){
                    for(int j = 0 ; j < this.blockHight ; j++){
                        averageArray[n][i][j] /= newFrequency[n];
                    }

                }
            }
            frequency = newFrequency;
            codeBook = averageArray;
        }
        return codeBook;

    }
    public int[][] compress(int[][] pixelsArray ,int imageWidth, int imageHight,int blockWidth , int blockHight , int codeBookSize ){
        //-------------------------
        //1-create the code book
        //-------------------------
        codeBook = LGBAlgorithm(pixelsArray,imageHight,imageWidth, blockWidth, blockHight, codeBookSize);
        //-------------------------
        //2-create the compressed matrix 
        //-------------------------
        int[][] compressedMatrix = new int[imageWidth/blockWidth][imageHight/blockHight];
        int widthStep = 0;
        int hightStep;
        int maxNumberOfWidthSteps = imageWidth/blockWidth;
        int maxNumberOfHightSteps = imageHight/blockHight;
        while (widthStep < maxNumberOfWidthSteps) {
            hightStep = 0;
            while (hightStep < maxNumberOfHightSteps) {
                /* 
                 _ _ _
                |_|   |
                |     |
                |_ _ _|
                */
                //2.2.1-calculate min distance
                double minDistance=0;
                int minBlockDistanceIndex = 0;
                for(int i = 0 ; i < codeBookSize ; i++ ){
                    double distance = distance(pixelsArray,widthStep,hightStep,codeBook,i);
                    if(i == 0){
                        minDistance = distance;
                    }
                    else if(distance < minDistance){
                        minDistance = distance;
                        minBlockDistanceIndex = i;
                    }
                }
                compressedMatrix[widthStep][hightStep] = minBlockDistanceIndex;
                System.out.println(minBlockDistanceIndex);
                hightStep++;
            }
            widthStep++;
        }
        return  compressedMatrix;
    }
    public double[][] decompress(int[][] compressedMatrix,double[][][] codeBook, int imageWidth, int imageHight,int blockWidth , int blockHight) {
        // Create a 2D array to hold the decompressed image
        double[][] decompressedImage = new double[imageWidth][imageHight];

        // Iterate through each block in the compressed matrix
        for (int widthStep = 0; widthStep < compressedMatrix.length; widthStep++) {
            for (int hightStep = 0; hightStep < compressedMatrix[0].length; hightStep++) {
                // Get the index of the codebook for the current block
                int codeBookIndex = compressedMatrix[widthStep][hightStep];
                System.out.println(codeBookIndex);

                // Fill the corresponding block in the decompressed image using the codebook
                for (int i = 0; i < blockWidth; i++) {
                    for (int j = 0; j < blockHight; j++) {
                        // Calculate the position in the decompressed image
                        int x = (widthStep * blockWidth) + i;
                        int y = (hightStep * blockHight) + j;

                        // Ensure we do not go out of bounds
                        if (x < imageWidth && y < imageHight) {
                            // Set the pixel value from the codebook
                            System.out.println(x + " " + y + " " + i + " " + j +" "+codeBook[codeBookIndex][i][j]);
                            decompressedImage[x][y] = (int) codeBook[codeBookIndex][i][j];
                        }
                    }
                }
            }
        }
        return decompressedImage;
    }
    public void setImageHight(int imageHight) {
        this.imageHight = imageHight;
    }
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }
    public void setBlockHight(int blockHight) {
        this.blockHight = blockHight;
    }
    public void setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;
    }

    public double[][][] getCodeBook() {
        return codeBook;
    }

    public void setCodeBook(double[][][] codeBook) {
        this.codeBook = codeBook;
    }
}
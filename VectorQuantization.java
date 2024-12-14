import java.awt.image.BufferedImage;

class VectorQuantization{
    private int imageHight;
    private int imageWidth;
    private int blockHight;
    private int blockWidth;
    private int codeBookSize;
    private int numberOfBlocksInCodeBook = 0;
//_______________________________________________________________________________________________________________________
    private double[][][] split(double[][][] oldCodeBook){
        double[][][] newCodeBook = new double[numberOfBlocksInCodeBook*2][blockWidth][blockHight];
        for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
            for(int i = 0 ; i < this.blockWidth ; i++){
                for(int j = 0 ; j < this.blockHight ; j++){
                    newCodeBook[(2*n)+1][i][j] = Math.ceil(oldCodeBook[n][i][j])+1;
                    newCodeBook[2*n][i][j] = Math.ceil(oldCodeBook[n][i][j]);
                }
            }
        }
        // for(int n = 0 ; n < this.numberOfBlocksInCodeBook *2; n++){
        //     for(int i = 0 ; i < this.blockWidth ; i++){
        //         for(int j = 0 ; j < this.blockHight ; j++){
        //             System.out.print(newCodeBook[n][i][j]+" ");
        //         }
        //         System.out.println();
        //         System.out.println();
        //     }
        //     System.out.println("_____________________________________________");
        // }
        // System.out.println("_____________________*________________________");
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
                //pixelsArray[(i%blockWidth)+(blockWidth*widthStep)][(j%blockHight)+(blockHight*hightStep)]
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
                // System.out.println(i%blockWidth + " "+ j%blockHight + " "+ i + " "+ j + " "+arr[i][j]);
            }
        }
        double blocksNumber = (imageHight*imageWidth)/(blockHight*blockWidth);
        // System.out.println(blocksNumber);
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
            hightStep = 0;
            while (widthStep < maxNumberOfWidthSteps) {
                //System.out.println(widthStep);
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
                   // System.out.println("//////////////////////////////////////////////////////////////");
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
        // for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
        //     for(int i = 0 ; i < this.blockWidth ; i++){
        //         for(int j = 0 ; j < this.blockHight ; j++){
        //             System.out.print(codeBook[n][i][j]+" ");
        //         }
        //         System.out.println();
        //         System.out.println();
        //     }
        //     System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        // }
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
                   // System.out.println("//////////////////////////////////////////////////////////////");
                    hightStep ++;
                }
                widthStep ++;
            }
            for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
                if(newFrequency[n] != frequency[n]){
                    // System.out.println( frequency[n] +" "+newFrequency[n] );
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
    public void compress(BufferedImage image,int blockWidth , int blockHight , int codeBookSize ){
        //1-take the pixels of image and put it in 2d array
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixelsArray = new int[width][height];
        System.out.println(height+" "+width);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // System.out.println(i+" "+j);
                pixelsArray[i][j] = (image.getRGB(i,j) >> 16) & 0xFF;
            }
        }
        double [][][] arr = LGBAlgorithm(pixelsArray,height,width, blockWidth, blockHight, codeBookSize);
        for (int n = 0; n < codeBookSize; n++) {
            for (int i = 0; i < blockWidth; i++) {
                for (int j = 0; j < blockHight; j++) {
                    System.out.print(arr[n][i][j]+" ");
                }
                System.out.println();
            }
            System.out.println("----------------------");
        }
    }
    public void decompress(){

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
}
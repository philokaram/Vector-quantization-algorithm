import java.awt.image.BufferedImage;

class VectorQuantization{
    private int imageHight;
    private int imageWidth;
    private int blockHight;
    private int blockWidth;
    private int codeBookSize;
    private int numberOfBlocksInCodeBook = 0;
    private double[][][] split(double[][][] oldCodeBook){
        double[][][] newCodeBook = new double[numberOfBlocksInCodeBook*2][blockWidth][blockHight];
        for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
            for(int i = 0 ; i < this.blockWidth ; i++){
                for(int j = 0 ; j < this.blockHight ; j++){
                    newCodeBook[(2*n)+1][i][j] = Math.floor(oldCodeBook[n][i][j]);
                    newCodeBook[2*n][i][j] = Math.ceil(oldCodeBook[n][i][j]);
                }
            }
        }

        return newCodeBook;
    }
    private double[][] LGBAlgorithm(int[][] arr,int imageHight , int imageWidth ,int blockWidth , int blockHight , int codeBookSize){
        this.imageHight = imageHight;
        this.imageWidth = imageWidth;
        this.blockHight = blockHight;
        this.blockWidth = blockWidth;
        this.codeBookSize = codeBookSize;
        double[][][] codeBook = new double[1][blockWidth][blockHight];
        double[][] initialBlock = new double[blockWidth][blockHight];
        //1-calculate the average
        for(int i = 0; i < imageWidth ; i++){
            for(int j = 0 ; j < imageHight ; j++){
                initialBlock[i%blockWidth][j%blockHight] += arr[i][j];
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
        //2-split
        this.numberOfBlocksInCodeBook = 1;
        while (numberOfBlocksInCodeBook < codeBookSize) {
            codeBook = split(codeBook);
            numberOfBlocksInCodeBook *=2;
        }
        for(int n = 0 ; n < this.numberOfBlocksInCodeBook ; n++){
            for(int i = 0 ; i < this.blockWidth ; i++){
                for(int j = 0 ; j < this.blockHight ; j++){
                    System.out.print(codeBook[n][i][j]+" ");
                }
                System.out.println();
                System.out.println();
            }
            System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        }
        return initialBlock;

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
        double [][] arr = LGBAlgorithm(pixelsArray,height,width, blockWidth, blockHight, codeBookSize);
        for (int i = 0; i < blockWidth; i++) {
            for (int j = 0; j < blockHight; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
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
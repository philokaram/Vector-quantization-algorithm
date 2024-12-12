import java.awt.image.BufferedImage;

class VectorQuantization{
    public void compress(BufferedImage image,int blockWidth , int blockHight , int codeBookSize ){
        int[][] pixelsArray = new int[image.getHeight()][image.getWidth()];
        System.out.println("w: "+image.getWidth()+"  h: "+image.getHeight());
        for (int i = 1; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                pixelsArray[i][j] = image.getRGB(i,j);
                System.out.print(i+" "+j+" "+Integer.toHexString(pixelsArray[i][j]) + " ");
            }
            System.out.println("\n");
        }
    }
    public void decompress(){

    }
}
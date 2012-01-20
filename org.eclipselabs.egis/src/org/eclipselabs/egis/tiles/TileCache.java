/*
* TileCache.java
*
* Created on January 2, 2007, 7:17 PM
*
* To change this template, choose Tools | Template Manager
* and open the template in the editor.
*/

package org.eclipselabs.egis.tiles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
* An implementation only class for now. For internal use only.
*
* @author joshua.marinacci@sun.com
*/
public class TileCache {
  private Map<URI, Image> imgmap = new HashMap<URI, Image>();
  private LinkedList<URI> imgmapAccessQueue = new LinkedList<URI>();
  private int imagesize = 0;
  private Map<URI, byte[]> bytemap = new HashMap<URI, byte[]>();
  private LinkedList<URI> bytemapAccessQueue = new LinkedList<URI>();
  private int bytesize = 0;

    
  public TileCache() { }

  /**
   * Put a tile image into the cache. This puts both a buffered image and array of bytes that make up the compressed image.
   *
   * @param uri
   *            URI of image that is being stored in the cache
   * @param bimg
   *            bytes of the compressed image, ie: the image file that was loaded over the network
   * @param img
   *            image to store in the cache
   */
  public void put(URI uri, byte[] bimg, Image img) {
      synchronized (bytemap) {
          while (bytesize > 1000 * 1000 * 50) {
              URI olduri = bytemapAccessQueue.removeFirst();
              byte[] oldbimg = bytemap.remove(olduri);
              bytesize -= oldbimg.length;
              p("removed 1 img from byte cache");
          }

          bytemap.put(uri, bimg);
          bytesize += bimg.length;
          bytemapAccessQueue.addLast(uri);
      }
      addToImageCache(uri, img);
  }

  /**
   * Returns a buffered image for the requested URI from the cache. This method must return null if the image is not in the cache. If the image is unavailable
   * but it's compressed version *is* available, then the compressed version will be expanded and returned.
   *
   * @param uri URI of the image previously put in the cache
   * @return the image matching the requested URI, or null if not available
   * @throws java.io.IOException
   */
  public Image get(URI uri) throws IOException {
      synchronized (imgmap) {
          if (imgmap.containsKey(uri)) {
              imgmapAccessQueue.remove(uri);
              imgmapAccessQueue.addLast(uri);
              return imgmap.get(uri);
          }
      }
      synchronized (bytemap) {
          if (bytemap.containsKey(uri)) {
              p("retrieving from bytes");
              bytemapAccessQueue.remove(uri);
              bytemapAccessQueue.addLast(uri);
              Image img = new Image(null, new ByteArrayInputStream(bytemap.get(uri))); 
              addToImageCache(uri, img);
              return img;
          }
      }
      return null;
  }

  /**
   * Request that the cache free up some memory. How this happens or how much memory is freed is up to the TileCache implementation. Subclasses can implement
   * their own strategy. The default strategy is to clear out all buffered images but retain the compressed versions.
   */
  public void needMoreMemory() {
      imgmap.clear();
      p("HACK! need more memory: freeing up memory");
  }

    
    
  private void addToImageCache(final URI uri, final Image img) {
      synchronized (imgmap) {
          while (imagesize > 1000 * 1000 * 50) {
              URI olduri = imgmapAccessQueue.removeFirst();
              Image oldimg = imgmap.remove(olduri);
              Rectangle bounds = oldimg.getBounds();
              imagesize -= bounds.width * bounds.height * 4;
              p("removed 1 img from image cache");
          }

          imgmap.put(uri, img);
          Rectangle bounds = img.getBounds();
          imagesize += bounds.width * bounds.height * 4;
          imgmapAccessQueue.addLast(uri);
      }
      p("added to cache: " + " uncompressed = " + imgmap.keySet().size() + " / " + imagesize / 1000 + "k" + " compressed = " + bytemap.keySet().size()
              + " / " + bytesize / 1000 + "k");
  }

  private void p(String string) {
        //System.out.println(string);
  }

}

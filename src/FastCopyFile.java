// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class FastCopyFile
{
  static public void main( String args[] ) throws Exception {
    if (args.length<2) {
      System.err.println( "Usage: java FastCopyFile infile outfile" );
      System.exit( 1 );
    }

    String infile = args[0];
    String outfile = args[1];

    FileInputStream fin = new FileInputStream( infile );
    FileOutputStream fout = new FileOutputStream( outfile );

    FileChannel fcin = fin.getChannel();
    FileChannel fcout = fout.getChannel();

    long a = System.currentTimeMillis();
    ByteBuffer buffer = ByteBuffer.allocateDirect( 1024*1024 );

    System.out.println(System.currentTimeMillis() - a);
    a = System.currentTimeMillis();
    while (true) {
      buffer.clear();

      int r = fcin.read( buffer );

      if (r==-1) {
        break;
      }

      buffer.flip();

      fcout.write( buffer );
    }
    System.out.println(System.currentTimeMillis() - a);
    fcin.close();
    fcout.close();
  }
}

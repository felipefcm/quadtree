
package ffcm.quadtree.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ffcm.quadtree.QuadTreeTest;

public class DesktopLauncher 
{
	public static void main(String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "QuadTree Test";
		config.width = (int)(QuadTreeTest.V_WIDTH * QuadTreeTest.DESKTOP_SCALE);
		config.height = (int)(QuadTreeTest.V_HEIGHT * QuadTreeTest.DESKTOP_SCALE);
		
		new LwjglApplication(new QuadTreeTest(), config);
	}
}

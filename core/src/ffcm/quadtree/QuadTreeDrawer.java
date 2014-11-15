
package ffcm.quadtree;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class QuadTreeDrawer
{	
	public static boolean DrawTreeNodes = true;
	
	public static void Draw(ShapeRenderer shapeRenderer, QuadTree node)
	{
		shapeRenderer.setColor(Color.TEAL);
		shapeRenderer.begin(ShapeType.Filled);
		{
			//draw points
			for(int i = 0; i < node.data.size(); ++i)
			{
				Vector2 point = node.data.get(i);
				shapeRenderer.circle(point.x, point.y, 2.0f);
			}
		}
		shapeRenderer.end();
		
		if(DrawTreeNodes)
		{
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.begin(ShapeType.Line);
			{
				shapeRenderer.rect(node.area.x, node.area.y, node.area.width, node.area.height);
			}
			shapeRenderer.end();
		}
		
		if(node.child == null)
			return;
			
		for(int i = 0; i < node.child.length; ++i)
			Draw(shapeRenderer, node.child[i]);
	}
}

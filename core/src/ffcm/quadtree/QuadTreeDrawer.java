
package ffcm.quadtree;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class QuadTreeDrawer
{
    public static boolean drawRegions = true;

	private static ShapeRenderer shapeRenderer;

	public static void DrawTree(ShapeRenderer renderer, QuadTree node)
	{
		shapeRenderer = renderer;

        if(drawRegions)
        {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.begin(ShapeType.Line);
            {
                DrawRegion(node);
            }
            shapeRenderer.end();
        }

        shapeRenderer.setColor(Color.TEAL);
        shapeRenderer.begin(ShapeType.Filled);
        {
            DrawData(node);
        }
        shapeRenderer.end();
	}

	private static void DrawData(QuadTree node)
	{
		if(node.child != null)
		{
			for(QuadTree child : node.child)
				DrawData(child);
		}
		else
		{
			for(QuadTreeData data : node.data)
				shapeRenderer.circle(data.point.x, data.point.y, 4.0f);
		}
	}

	private static void DrawRegion(QuadTree node)
	{
		shapeRenderer.rect(node.area.x, node.area.y, node.area.width, node.area.height);

		if(node.child != null)
			for(QuadTree child : node.child)
				DrawRegion(child);
	}
}

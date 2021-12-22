
package ffcm.quadtree;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Date;

public class QuadTreeTest extends ApplicationAdapter 
{
	public static final int V_WIDTH = 1024;
	public static final int V_HEIGHT = 1024;
	public static final float DESKTOP_SCALE = 0.9f;
	
	public static FitViewport viewport;
	private OrthographicCamera camera;

	private ShapeRenderer shapeRenderer;
	
	private QuadTree quadTree;
	
	private QuadTreeData[] selectedPoints;
	private int numPoints = 0;
	
	@Override
	public void create() 
	{
		camera = new OrthographicCamera();
		viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);

		shapeRenderer = new ShapeRenderer();
		
		quadTree = new QuadTree(new Rectangle(0, 0, V_WIDTH + 1, V_HEIGHT + 1));
		
		long seed = new Date().getTime();
		MathUtils.random.setSeed(seed);
		Gdx.app.log("QuadTreeTest", "Random seed: " + seed);
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		Gdx.input.setInputProcessor
		(
			new InputAdapter()
			{
				public boolean touchDown(int screenX, int screenY, int pointer, int button)
				{
					Vector2 worldPos = viewport.unproject(new Vector2(screenX, screenY));
					
					if(button == Input.Buttons.LEFT)
					{
						quadTree.Add(new QuadTreeData(worldPos));
						numPoints++;
					}
					else if(button == Input.Buttons.RIGHT)
					{
						int boxSize = 50;
						
						selectedPoints = quadTree.SearchArea(new Rectangle(worldPos.x - boxSize * 0.5f, worldPos.y - boxSize * 0.5f, boxSize, boxSize));
					}
					
					return true;
				}
				
				public boolean keyDown(int keycode)
				{
					if(keycode == Input.Keys.R)
					{
						for(int i = 0; i < 100; ++i)
						{
							Vector2 randomPoint = new Vector2(MathUtils.random.nextFloat() * viewport.getWorldWidth(), MathUtils.random.nextFloat() * viewport.getWorldHeight());
							
							quadTree.Add(new QuadTreeData(randomPoint));
							numPoints++;
						}
						
						return true;
					}
					else if(keycode == Input.Keys.Y)
					{
						for(int i = 0; i < 5000; ++i)
						{
							Vector2 randomPoint = new Vector2(MathUtils.random.nextFloat() * viewport.getWorldWidth(), MathUtils.random.nextFloat() * viewport.getWorldHeight());
							
							quadTree.Add(new QuadTreeData(randomPoint));
							numPoints++;
						}
						
						return true;
					}
					else if(keycode == Input.Keys.T)
					{
						QuadTreeDrawer.drawRegions = !QuadTreeDrawer.drawRegions;
					}
					
					return false;
				}
			}
		);
	}

	@Override
	public void resize(int width, int height)
	{
		viewport.update(width, height, true);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}
	
	@Override
	public void render() 
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		QuadTreeDrawer.DrawTree(shapeRenderer, quadTree);
		
		if(selectedPoints != null)
		{
			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.begin(ShapeType.Filled);
			{
				for(QuadTreeData selectedPoint : selectedPoints)
				{
					shapeRenderer.circle(selectedPoint.point.x, selectedPoint.point.y, 2.5f);
				}
			}
			shapeRenderer.end();
		}
	}
}

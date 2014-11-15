
package ffcm.quadtree;

import java.util.Date;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class QuadTreeTest extends ApplicationAdapter 
{
	public static final int V_WIDTH = 640;
	public static final int V_HEIGHT = 480;
	public static final float DESKTOP_SCALE = 2.0f;
	
	public static FitViewport viewport;
	private OrthographicCamera camera;
	
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	
	private QuadTree quadTree;
	
	private Vector2[] selectedPoints;
	private int numPoints = 0;
	
	private Random random;
	
	@Override
	public void create() 
	{
		camera = new OrthographicCamera();
		viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		spriteBatch = new SpriteBatch(200);
		shapeRenderer = new ShapeRenderer(200);
		
		quadTree = new QuadTree(new Rectangle(0, 0, V_WIDTH + 1, V_HEIGHT + 1));
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		long seed = new Date().getTime();
		random = new Random(seed);
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
						quadTree.Add(worldPos);
						numPoints++;
					}
					else if(button == Input.Buttons.RIGHT)
					{	
						QuadTree.numComp = 0;
						
						selectedPoints = quadTree.SearchArea(new Rectangle(worldPos.x - 50, worldPos.y - 50, 100, 100));
						
						if(numPoints > 0)
							Gdx.app.log("QuadTreeTest", "Comparations: " + QuadTree.numComp + " of " + numPoints + " " + (int)(QuadTree.numComp / (float) numPoints * 100.0f) + "%");
					}
					
					return true;
				};
				
				public boolean keyUp(int keycode) 
				{
					if(keycode == Input.Keys.R)
					{
						for(int i = 0; i < 100; ++i)
						{
							Vector2 randomPoint = new Vector2(random.nextFloat() * viewport.getWorldWidth(), random.nextFloat() * viewport.getWorldHeight());
							
							quadTree.Add(randomPoint);
							numPoints++;
						}
						
						return true;
					}
					else if(keycode == Input.Keys.X)
					{
						Vector2 worldPos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
						
						QuadTree.numComp = 0;
						
						selectedPoints = quadTree.GetPathToPoint(worldPos);
						
						if(numPoints > 0)
							Gdx.app.log("QuadTreeTest", "Comparations: " + QuadTree.numComp + " of " + numPoints + " " + (int)(QuadTree.numComp / (float) numPoints * 100.0f) + "%");
						
						return true;
					}
					else if(keycode == Input.Keys.T)
					{
						QuadTreeDrawer.DrawTreeNodes = !QuadTreeDrawer.DrawTreeNodes;
						return true;
					}
					
					return false;
				};
			}
		);
	}

	@Override
	public void resize(int width, int height)
	{
	}
	
	@Override
	public void render() 
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		QuadTreeDrawer.Draw(shapeRenderer, quadTree);
		
		if(selectedPoints != null)
		{
			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.begin(ShapeType.Filled);
			{
				for(int i = 0; i < selectedPoints.length; ++i)
				{
					shapeRenderer.circle(selectedPoints[i].x, selectedPoints[i].y, 3.0f);
				}
			}
			shapeRenderer.end();
		}
	}
}

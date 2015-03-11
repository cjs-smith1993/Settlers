package client.frontend.map;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.frontend.base.*;
import client.frontend.data.*;
import shared.definitions.*;
import shared.locations.*;

/**
 * Implementation for the map view
 */
@SuppressWarnings("serial")
public class MapView extends PanelView implements IMapView
{

	private MapComponent map;
	private MapOverlay overlay;

	public MapView()
	{

		this.setLayout(new BorderLayout());

		this.map = new MapComponent();

		this.add(this.map, BorderLayout.CENTER);
	}

	@Override
	public IMapController getController()
	{
		return (IMapController) super.getController();
	}

	@Override
	public void setController(IController controller)
	{

		super.setController(controller);

		this.map.setController(controller);
	}

	@Override
	public void addHex(HexLocation hexLoc, HexType hexType)
	{
		this.map.addHex(hexLoc, hexType);
	}

	@Override
	public void addNumber(HexLocation hexLoc, int num)
	{
		this.map.addNumber(hexLoc, num);
	}

	@Override
	public void addPort(EdgeLocation edgeLoc, PortType portType)
	{
		this.map.placePort(edgeLoc, portType);
	}

	@Override
	public void placeRoad(EdgeLocation edgeLoc, CatanColor color)
	{
		this.map.placeRoad(edgeLoc, color);
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc, CatanColor color)
	{
		this.map.placeSettlement(vertLoc, color);
	}

	@Override
	public void placeCity(VertexLocation vertLoc, CatanColor color)
	{
		this.map.placeCity(vertLoc, color);
	}

	@Override
	public void placeRobber(HexLocation hexLoc)
	{
		this.map.placeRobber(hexLoc);
	}

	@Override
	public void startDrop(PieceType pieceType, CatanColor pieceColor,
			boolean isCancelAllowed)
	{

		this.overlay = new MapOverlay(this.map);
		this.overlay.setController(this.overlayController);
		this.overlay.startDrop(pieceType, pieceColor, isCancelAllowed);
		this.overlay.showModal();
	}

	public void removeRoad(EdgeLocation edge) {
		this.map.removeRoad(edge);
	}

	private IMapController overlayController = new IMapController() {

		@Override
		public IView getView()
		{
			assert false;
			return null;
		}

		@Override
		public boolean canPlaceRoad(EdgeLocation edgeLoc)
		{
			return MapView.this.getController().canPlaceRoad(edgeLoc);
		}

		@Override
		public boolean canPlaceSettlement(VertexLocation vertLoc)
		{
			return MapView.this.getController().canPlaceSettlement(vertLoc);
		}

		@Override
		public boolean canPlaceCity(VertexLocation vertLoc)
		{
			return MapView.this.getController().canPlaceCity(vertLoc);
		}

		@Override
		public boolean canPlaceRobber(HexLocation hexLoc)
		{
			return MapView.this.getController().canPlaceRobber(hexLoc);
		}

		@Override
		public void placeRoad(EdgeLocation edgeLoc)
		{

			this.closeModal();
			MapView.this.getController().placeRoad(edgeLoc);
		}

		@Override
		public void placeSettlement(VertexLocation vertLoc)
		{

			this.closeModal();
			MapView.this.getController().placeSettlement(vertLoc);
		}

		@Override
		public void placeCity(VertexLocation vertLoc)
		{

			this.closeModal();
			MapView.this.getController().placeCity(vertLoc);
		}

		@Override
		public void placeRobber(HexLocation hexLoc)
		{

			this.closeModal();
			MapView.this.getController().placeRobber(hexLoc);
		}

		@Override
		public void startMove(PieceType pieceType, boolean isFree,
				boolean allowDisconnected)
		{
			assert false;
		}

		@Override
		public void cancelMove()
		{

			this.closeModal();
			MapView.this.getController().cancelMove();
		}

		@Override
		public void playSoldierCard()
		{
			assert false;
		}

		@Override
		public void playRoadBuildingCard()
		{
			assert false;
		}

		@Override
		public void robPlayer(RobPlayerInfo victim)
		{
			assert false;
		}

		private void closeModal()
		{
			MapView.this.overlay.cancelDrop();
			MapView.this.overlay.closeModal();
		}
	};

	private static class MapOverlay extends OverlayView
	{

		private final int LABEL_TEXT_SIZE = 40;
		private final int BUTTON_TEXT_SIZE = 28;
		private final int BORDER_WIDTH = 10;

		private MapComponent mainMap;
		private JLabel label;
		private MapComponent map;
		private JButton cancelButton;

		public MapOverlay(MapComponent mainMap)
		{

			super();

			this.mainMap = mainMap;
		}

		@Override
		public IMapController getController()
		{
			return (IMapController) super.getController();
		}

		public void startDrop(PieceType pieceType, CatanColor pieceColor,
				boolean isCancelAllowed)
		{

			this.setOpaque(false);
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createLineBorder(Color.black,
					this.BORDER_WIDTH));

			this.label = new JLabel(this.getLabelText(pieceType), JLabel.CENTER);
			this.label.setOpaque(true);
			this.label.setBackground(Color.white);
			Font labelFont = this.label.getFont();
			labelFont = labelFont.deriveFont(labelFont.getStyle(),
					this.LABEL_TEXT_SIZE);
			this.label.setFont(labelFont);

			this.map = this.mainMap.copy();
			this.map.setController(this.getController());

			int prefWidth = (int) (this.mainMap.getScale() * this.mainMap.getPreferredSize()
					.getWidth());
			int prefHeight = (int) (this.mainMap.getScale() * this.mainMap.getPreferredSize()
					.getHeight());
			Dimension prefSize = new Dimension(prefWidth, prefHeight);
			this.map.setPreferredSize(prefSize);

			this.add(this.label, BorderLayout.NORTH);
			this.add(this.map, BorderLayout.CENTER);

			if (isCancelAllowed)
			{

				this.cancelButton = new JButton("Cancel");
				Font buttonFont = this.cancelButton.getFont();
				buttonFont = buttonFont.deriveFont(buttonFont.getStyle(),
						this.BUTTON_TEXT_SIZE);
				this.cancelButton.setFont(buttonFont);
				this.cancelButton.addActionListener(this.cancelButtonListener);
				this.add(this.cancelButton, BorderLayout.SOUTH);
			}

			this.map.startDrop(pieceType, pieceColor);
		}

		private ActionListener cancelButtonListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				MapOverlay.this.getController().cancelMove();
			}
		};

		public void cancelDrop()
		{

			this.map.cancelDrop();
		}

		private String getLabelText(PieceType pieceType)
		{

			switch (pieceType)
			{
			case ROAD:
				return "Place a Road!";
			case SETTLEMENT:
				return "Place a Settlement!";
			case CITY:
				return "Place a City!";
			case ROBBER:
				return "Move the Robber!";
			default:
				assert false;
				return "";
			}
		}
	}

}

package game;

import game.rooms.*;
import game.squares.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Board {
	private static final int width = 24;
	private static final int height = 25;
	public Square[][] board = new Square[width][height];
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<PlayerSpawnSquare> spawnList = new ArrayList<>();
	private ArrayList<DoorSquare> doorList = new ArrayList<>();
	private Map<Player, Square> playerMap = new HashMap<>();

	public Board() {
		String boardFile = "src/gameBoard.txt";

		addAndLinkRooms();

		try {
			Scanner sc = new Scanner(new File(boardFile));
			this.board = parseBoard(sc);
		} catch (FileNotFoundException e) {
			System.out.println("No file found with name " + boardFile);
		}

		printToConsole();
		addDoors();
		// testDoors();
		// testStairs();
	}

	private Square[][] parseBoard(Scanner sc) {
		Square[][] returnBoard;

		returnBoard = parseSquares(sc);

		return returnBoard;
	}

	private Square[][] parseSquares(Scanner sc) {
		String next;
		Square[][] board = new Square[height][width];
		DoorSquare tempDoor = null;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				next = sc.next();
				if (next.equals(" ")) {
					next = sc.next();
				}

				RoomSquare tempSquare;
				switch (next) {
				case "#":
					board[i][j] = new WallSquare(i, j);
					break;
				case "K":
					tempSquare = new RoomSquare(i, j, 'K', "Kitchen");
					board[i][j] = tempSquare;
					getRoom("Kitchen").addSquare(tempSquare);
					break;
				case "-":
					board[i][j] = new WalkwaySquare(i, j);
					break;
				case "B":
					tempSquare = new RoomSquare(i, j, 'B', "Ballroom");
					board[i][j] = tempSquare;
					getRoom("Ballroom").addSquare(tempSquare);
					break;
				case "C":
					tempSquare = new RoomSquare(i, j, 'C', "Conservatory");
					board[i][j] = tempSquare;
					getRoom("Conservatory").addSquare(tempSquare);
					break;
				case "D":
					tempSquare = new RoomSquare(i, j, 'D', "Dining Room");
					board[i][j] = tempSquare;
					getRoom("Dining Room").addSquare(tempSquare);
					break;
				case "G":
					tempSquare = new RoomSquare(i, j, 'G', "Billiard Room");
					board[i][j] = tempSquare;
					getRoom("Billiard Room").addSquare(tempSquare);
					break;
				case "L":
					tempSquare = new RoomSquare(i, j, 'L', "Library");
					board[i][j] = tempSquare;
					getRoom("Library").addSquare(tempSquare);
					break;
				case "T":
					tempSquare = new RoomSquare(i, j, 'T', "Lounge");
					board[i][j] = tempSquare;
					getRoom("Lounge").addSquare(tempSquare);
					break;
				case "H":
					tempSquare = new RoomSquare(i, j, 'H', "Hall");
					board[i][j] = tempSquare;
					getRoom("Hall").addSquare(tempSquare);
					break;
				case "S":
					tempSquare = new RoomSquare(i, j, 'S', "Study");
					board[i][j] = tempSquare;
					getRoom("Study").addSquare(tempSquare);
					break;
				case "<":
					tempDoor = new DoorSquare(i, j, '<');
					board[i][j] = tempDoor;
					doorList.add(tempDoor);
					break;
				case ">":
					tempDoor = new DoorSquare(i, j, '>');
					board[i][j] = tempDoor;
					doorList.add(tempDoor);
					break;
				case "v":
					tempDoor = new DoorSquare(i, j, 'v');
					board[i][j] = tempDoor;
					doorList.add(tempDoor);
					break;
				case "^":
					tempDoor = new DoorSquare(i, j, '^');
					board[i][j] = tempDoor;
					doorList.add(tempDoor);
					break;
				default:
					PlayerSpawnSquare tempSpawn = new PlayerSpawnSquare(i, j,
							'@');
					board[i][j] = tempSpawn;
					spawnList.add(tempSpawn);
					break;
				}
			}
		}
		return board;
	}

	public void testStairs() {
		for (Room r : roomList) {
			if (r.getStairs() != null) {
				System.out.println(r.getName() + " --> "
						+ r.getStairs().getName());
			} else {
				System.out.println("No stairs in " + r.getName());
			}
		}
	}

	public void testDoors() {
		for (DoorSquare d : doorList) {
			System.out.println(d.getX() + "," + d.getY() + "," + d.getID());
		}

		for (Room r : roomList) {
			System.out.print(r.getDoorList());
		}
	}

	public void printToConsole() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[i][j].getPlayer() != null) {
					System.out.print(board[i][j].getPlayer().getPlayerNum());
				} else {
					System.out.print(board[i][j].getID());
				}
			}
			System.out.println();
		}
	}

	public void addAndLinkRooms() {
		// Add all rooms to list
		roomList.add(new Ballroom());
		roomList.add(new DiningRoom());
		roomList.add(new BilliardRoom());
		roomList.add(new Library());
		roomList.add(new Hall());
		roomList.add(new Kitchen());
		roomList.add(new Study());
		roomList.add(new Lounge());
		roomList.add(new Conservatory());

		// Link stairs to necessary rooms
		getRoom("Kitchen").addStairs(getRoom("Study"));
		getRoom("Study").addStairs(getRoom("Kitchen"));
		getRoom("Lounge").addStairs(getRoom("Conservatory"));
		getRoom("Conservatory").addStairs(getRoom("Lounge"));
	}

	public void addDoors() {
		RoomSquare tempRoom;
		for (DoorSquare d : doorList) {
			switch (d.getID()) {
			case '<':
				tempRoom = (RoomSquare) board[d.getX()][d.getY() + 1];
				d.addRoom(getRoom(tempRoom.getName()));
				break;
			case '>':
				tempRoom = (RoomSquare) board[d.getX()][d.getY() - 1];
				d.addRoom(getRoom(tempRoom.getName()));
				break;
			case 'v':
				tempRoom = (RoomSquare) board[d.getX() - 1][d.getY()];
				d.addRoom(getRoom(tempRoom.getName()));
				break;
			case '^':
				tempRoom = (RoomSquare) board[d.getX() + 1][d.getY()];
				d.addRoom(getRoom(tempRoom.getName()));
				break;
			default:
				break;
			}
		}

		for (DoorSquare d : doorList) {
			getRoom(d.getRoom().getName()).addDoor(d);
		}

	}

	public Square addPlayer(Player p) {
		playerMap.put(p, spawnList.get(p.getPlayerNum() - 1));
		spawnList.get(p.getPlayerNum() - 1).setPlayer(p);
		return spawnList.get(p.getPlayerNum() - 1);
	}

	public Room getRoom(String name) {
		for (Room r : roomList) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}

	public Square playerMove(Player p, String dir) {
		Square tempSquare;
		switch (dir) {
		case "u":
			tempSquare = board[playerMap.get(p).getX() - 1][playerMap.get(p)
					.getY()];
			playerMap.get(p).setPlayer(null);
			tempSquare.setPlayer(p);
			playerMap.put(p, tempSquare);

			break;
		case "d":
			tempSquare = board[playerMap.get(p).getX() + 1][playerMap.get(p)
					.getY()];
			playerMap.get(p).setPlayer(null);
			tempSquare.setPlayer(p);
			playerMap.put(p, tempSquare);
			break;
		case "l":
			tempSquare = board[playerMap.get(p).getX()][playerMap.get(p).getY() - 1];
			playerMap.get(p).setPlayer(null);
			tempSquare.setPlayer(p);
			playerMap.put(p, tempSquare);
			break;
		case "r":
			tempSquare = board[playerMap.get(p).getX()][playerMap.get(p).getY() + 1];
			playerMap.get(p).setPlayer(null);
			tempSquare.setPlayer(p);
			playerMap.put(p, tempSquare);
			break;
		default:
			break;
		}
		return playerMap.get(p);
	}

	public Square enterRoom(Player p, Room r) {
		p.getLocation().setPlayer(null);

		switch (r.getName()) {
		case "Kitchen":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 3 && rs.getY() == p.getPlayerNum()) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		case "Ballroom":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 4 && rs.getY() == p.getPlayerNum() + 8) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		case "Conservatory":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 3 && rs.getY() == p.getPlayerNum() + 17) {
					rs.setPlayer(p);
					return rs;
				}

			}
			break;
		case "Dining Room":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 13 && rs.getY() == p.getPlayerNum() + 1) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		case "Billiard Room":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 10 && rs.getY() == p.getPlayerNum() + 18) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		case "Library":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 15 && rs.getY() == p.getPlayerNum() + 17) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		case "Lounge":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 21 && rs.getY() == p.getPlayerNum()) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		case "Hall":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 21 && rs.getY() == p.getPlayerNum() + 8) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		case "Study":
			for (RoomSquare rs : r.getSquareList()) {
				if (rs.getX() == 22 && rs.getY() == p.getPlayerNum() + 17) {
					rs.setPlayer(p);
					return rs;
				}
			}
			break;
		default:
			break;
		}
		return null;
	}

	public boolean checkMove(Player p, String dir) {
		Square current = p.getLocation();
		switch (dir) {
		case "u":
			return (board[current.getX() - 1][current.getY()] instanceof DoorSquare)
					|| (board[current.getX() - 1][current.getY()] instanceof WalkwaySquare);
		case "d":
			return (board[current.getX() + 1][current.getY()] instanceof DoorSquare)
					|| (board[current.getX() + 1][current.getY()] instanceof WalkwaySquare);
		case "l":
			return (board[current.getX()][current.getY() - 1] instanceof DoorSquare)
					|| (board[current.getX()][current.getY() - 1] instanceof WalkwaySquare);
		case "r":
			return (board[current.getX()][current.getY() + 1] instanceof DoorSquare)
					|| (board[current.getX()][current.getY() + 1] instanceof WalkwaySquare);
		default:
			return false;
		}
	}

	// Getters
	public ArrayList<Room> getRoomList() {
		return this.roomList;
	}

	public ArrayList<PlayerSpawnSquare> getSpawnList() {
		return this.spawnList;
	}

	public ArrayList<DoorSquare> getDoorList() {
		return this.doorList;
	}

	public Map<Player, Square> getPlayerMap() {
		return this.playerMap;
	}

	public static void main(String[] args) {
		new Board();
	}
}

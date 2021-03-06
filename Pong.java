package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Pong implements KeyListener {

    Thread t = null;
    
    JFrame j = new JFrame();
    
    JPanel p = new JPanel();
    
    Ball ball;
    
    int lives = 0;
    
    Paddle paddle = new Paddle();

    Paddle enemyPaddle = new Paddle();

    int hits = 0;
    
    public class Square {
        int x, y;
        int width = 80;
        int height = 45;
        Color color = Color.WHITE;
        public boolean isHit() {
            if(ball.x >= x && ball.x <= x + width && ball.y >= y && ball.y <= y + height) {
                return true;
            }
            return false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    long time = 0;
    long timet = 0;
    
    String vk = "";
    
    boolean menu = true;
    int a = 1;
    int b = 1;

   @Override
    public void keyPressed(KeyEvent e) {
        if(menu)
        {
            if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(a == 1) {
                    a = 2;
                }
                else if(a == 2)
                    a = 3;
                else if(a == 3)
                    a = 1;
            }
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                if(b == 1) {
                    b = 2;
                }
                else if(b == 2)
                    b = 3;
                else if(b == 3)
                    b = 4;
                else if(b == 4)
                    b = 1;
            }
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                menu = false;
                lives = lives2 = 0;
                t = new Thread() {
                    public void run() {
                        while(true) {
                            if(a==1) {
                                if(ball.x < enemyPaddle.x+enemyPaddle.width/2) {
                                    enemyPaddle.move_left();
                                }
                                if(ball.x > enemyPaddle.x+enemyPaddle.width/2) {
                                    enemyPaddle.move_right();
                                }
                            } else if(a==2) {
                                if(ball.x < enemyPaddle.x+enemyPaddle.width/2) {
                                    enemyPaddle.move_left_mid();
                                }
                                if(ball.x > enemyPaddle.x+enemyPaddle.width/2) {
                                    enemyPaddle.move_right_mid();
                                }
                            } else if(a==3) {
                                if(ball.x < enemyPaddle.x+enemyPaddle.width/2) {
                                    enemyPaddle.move_left();
                                    enemyPaddle.move_left();
                                }
                                if(ball.x > enemyPaddle.x+enemyPaddle.width/2) {
                                    enemyPaddle.move_right();
                                    enemyPaddle.move_right();
                                }
                            }
                            if(vk.equals("left")) {
                                vk = "";
                                if(Math.abs(time - timet) < 2000) {
                                    if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                                        paddle.accel_left();
                                        ball.move();
                                    }
                                }
                            }
                            if(vk.equals("right")) {
                                vk = "";
                                if(Math.abs(time - timet) < 2000) {
                                    if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                                        paddle.accel_right();
                                        ball.move();
                                    }
                                }
                            }
                            if(b == 4)
                                j.setTitle("pong. {Free Points} Human: " + lives + " v. Computer: " + lives2);
                            else
                                j.setTitle("pong. {Points} Human: " + lives + " v. Computer: " + lives2);
                            try {
                                if(b == 1) {
                                    if(lives == 5) {
                                        menu = true;
                                        JOptionPane.showMessageDialog(null, "You win");
                                        t.stop();
                                    }
                                    if(lives2 == 5) {
                                        menu = true;
                                        JOptionPane.showMessageDialog(null, "You lose");
                                        t.stop();
                                    }
                                }
                                if(b == 2) {
                                    if(lives == 10) {
                                        menu = true;
                                        JOptionPane.showMessageDialog(null, "You win");
                                        t.stop();
                                    }
                                    if(lives2 == 10) {
                                        menu = true;
                                        JOptionPane.showMessageDialog(null, "You lose");
                                        t.stop();
                                    }
                                }
                                if(b == 3) {
                                    if(lives == 15) {
                                        menu = true;
                                        JOptionPane.showMessageDialog(null, "You win");
                                        t.stop();
                                    }
                                    if(lives2 == 15) {
                                        menu = true;
                                        JOptionPane.showMessageDialog(null, "You lose");
                                        t.stop();
                                    }
                                }
                                if(menu) {
                                    lives = lives2 = 0;

                                    tt = new Thread() {
                                        public void run() {
                                            while(true) {
                                                if(!menu)
                                                    break;
                                                Graphics g = p.getGraphics();
                                                try {
                                                    if(bgimg == null)
                                                        bgimg = ImageIO.read(getClass().getResourceAsStream("bg.jpg"));
                                                    g.drawImage(bgimg, 0, 0, 1000, 700, null);
                                                } catch(Exception e) {
                                                    e.printStackTrace();
                                                }
                                                g.setFont(new Font("arial",Font.BOLD,40));g.drawString("PONG", 10, 50);g.setFont(new Font("arial",Font.BOLD,20));
                                                if(a == 1) {
                                                    g.drawString("> Easy", 10, 100);
                                                    g.drawString("Medium", 10, 150);
                                                    g.drawString("Hard", 10, 200);
                                                    if(b == 1) {
                                                        g.drawString("> 5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 2) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("> 10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 3) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("> 15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 4) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("> free play", 10, 450);
                                                    }
                                                } else if(a == 2) {
                                                    g.drawString("Easy", 10, 100);
                                                    g.drawString("> Medium", 10, 150);
                                                    g.drawString("Hard", 10, 200);
                                                    if(b == 1) {
                                                        g.drawString("> 5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 2) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("> 10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 3) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("> 15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 4) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("> free play", 10, 450);
                                                    }
                                                } else if(a == 3) {
                                                    g.drawString("Easy", 10, 100);
                                                    g.drawString("Medium", 10, 150);
                                                    g.drawString("> Hard", 10, 200);
                                                    if(b == 1) {
                                                        g.drawString("> 5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 2) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("> 10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 3) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("> 15 point", 10, 400);
                                                        g.drawString("free play", 10, 450);
                                                    } else if(b == 4) {
                                                        g.drawString("5 point", 10, 300);
                                                        g.drawString("10 point", 10, 350);
                                                        g.drawString("15 point", 10, 400);
                                                        g.drawString("> free play", 10, 450);
                                                    }
                                                }
                                                g.dispose();
                                                try {
                                                    Thread.sleep(1000);
                                                } catch(InterruptedException ie) {

                                                }
                                            }
                                        }
                                    };
                                    tt.start();
                                }
                                Thread.sleep(50);
                                draw();
                                ball.move();
                                paddle.draw();
                                enemyPaddle.draw();
                                drawBall();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                t.start();
            }
        }
        if(!menu)
        {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if(JOptionPane.showConfirmDialog(null, "End current game?") == JOptionPane.YES_OPTION) {
                    menu = true;
                    t.stop();
                                    lives = lives2 = 0;

                    tt = new Thread() {
                        public void run() {
                            while(true) {
                                if(!menu)
                                    break;
                                Graphics g = p.getGraphics();
                                try {
                                    if(bgimg == null)
                                        bgimg = ImageIO.read(getClass().getResourceAsStream("bg.jpg"));
                                    g.drawImage(bgimg, 0, 0, 1000, 700, null);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                                g.setFont(new Font("arial",Font.BOLD,40));g.drawString("PONG", 10, 50);g.setFont(new Font("arial",Font.BOLD,20));
                                if(a == 1) {
                                    g.drawString("> Easy", 10, 100);
                                    g.drawString("Medium", 10, 150);
                                    g.drawString("Hard", 10, 200);
                                    if(b == 1) {
                                        g.drawString("> 5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 2) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("> 10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 3) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("> 15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 4) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("> free play", 10, 450);
                                    }
                                } else if(a == 2) {
                                    g.drawString("Easy", 10, 100);
                                    g.drawString("> Medium", 10, 150);
                                    g.drawString("Hard", 10, 200);
                                    if(b == 1) {
                                        g.drawString("> 5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 2) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("> 10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 3) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("> 15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 4) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("> free play", 10, 450);
                                    }
                                } else if(a == 3) {
                                    g.drawString("Easy", 10, 100);
                                    g.drawString("Medium", 10, 150);
                                    g.drawString("> Hard", 10, 200);
                                    if(b == 1) {
                                        g.drawString("> 5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 2) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("> 10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 3) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("> 15 point", 10, 400);
                                        g.drawString("free play", 10, 450);
                                    } else if(b == 4) {
                                        g.drawString("5 point", 10, 300);
                                        g.drawString("10 point", 10, 350);
                                        g.drawString("15 point", 10, 400);
                                        g.drawString("> free play", 10, 450);
                                    }
                                }
                                g.dispose();
                                try {
                                    Thread.sleep(1000);
                                } catch(InterruptedException ie) {

                                }
                            }
                        }
                    };
                    tt.start();
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                vk = "left";
                paddle.move_left();
                timet = new Date().getTime();
                if(Math.abs(time - timet) < 2000) {
                    if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                        paddle.accel_left();
                        ball.move();
                    }
                }
                time = new Date().getTime();
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                vk = "right";
                paddle.move_right();
                timet = new Date().getTime();
                if(Math.abs(time - timet) < 2000) {
                    if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                        paddle.accel_right();
                        ball.move();
                    }
                }
                time = new Date().getTime();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    Date date = new Date();
    
    public Pong() {
        j.requestFocus();
        j.setLayout(null);
        j.setBounds(0, 0, 1000, 700);
        p.setBounds(j.getBounds());
        j.add(p);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
        j.addKeyListener(this);
        draw();
        setBall();
        play();
    }
    
    public class Paddle {
        int x, y;
        int width = 140;
        int accel = 0;
        public Paddle() {
            x = 450;
            y = 630;
        }
        public void draw() {
            Graphics g = p.getGraphics();
            try {
                if(y==630) {
                    if(paddleimg1 == null)
                        paddleimg1 = ImageIO.read(getClass().getResourceAsStream("alleywaypaddle.jpg"));
                    g.drawImage(paddleimg1, x, y, paddle.width, 22, null);
                }
                else {
                    if(paddleimg2 == null)
                        paddleimg2 = ImageIO.read(getClass().getResourceAsStream("alleywaypaddle2.jpg"));
                    g.drawImage(paddleimg2, x, y, paddle.width, 22, null);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            g.dispose();
            }
        public void accel_left() {
            accel = -4;
            ball.plusx -= 6;
        }
        public void accel_right() {
            accel = 4;
            ball.plusx += 6;
        }
        public void move_left() {
            x-=20;
        }
        public void move_right() {
            x+=20;
        }
        public void move_left_mid() {
            x-=30;
        }
        public void move_right_mid() {
            x+=30;
        }
    }
    
    public class Ball {
        int x, y;
        int plusx, plusy;
        
        public Ball() {
            plusx = 1;
            plusy = 4;
        }
        
        public void move() {
            if(y > 1000 || y <= 20) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException ie) {
                    
                }
                if(y > 1000)
                    lives2++;
                if(y <= 20)
                    lives++;
                y = 100;
                ball.plusx = 7;
                ball.plusy = 12;
            }
            if(x <= 10) {
                plusx = -plusx;
            }
            if(x >= 970) {
                plusx = -plusx;
            }
            if(x >= paddle.x && x <= paddle.x + paddle.width && y >= paddle.y-10 && y <= paddle.y+10+14) {
                plusy = -plusy;
                hits++;
            }
            if(x >= enemyPaddle.x && x <= enemyPaddle.x + enemyPaddle.width && y >= enemyPaddle.y-10 && y <= enemyPaddle.y+10+20) {
                plusy = -plusy;
                hits++;
            }
            x+=plusx;
            y+=plusy;
        }
    }
    
    public void setBall() {
        ball = new Ball();
        
        ball.x = 500;
        ball.y = 250;
        
        ball.plusx = 7;
        ball.plusy = 12;
    }
    
    int level = 1;
    
    private void makeSound(String file) throws Exception {

        File audioFile = new File(file);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

        AudioFormat format = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);

        audioClip.open(audioStream);
        audioClip.start();
        audioStream.close();
    }

    int lives2 = 0;
    
    Thread tt = null;
    
    public void play() {
        enemyPaddle.x = 500;
        enemyPaddle.y = 50;
        lives = 0;
        tt = new Thread() {
            public void run() {
                while(true) {
                    if(!menu)
                        break;
                    Graphics g = p.getGraphics();
                    try {
                        Image img = ImageIO.read(getClass().getResourceAsStream("bg.jpg"));
                        g.drawImage(img, 0, 0, 1000, 700, null);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    g.setFont(new Font("arial",Font.BOLD,40));g.drawString("PONG", 10, 50);g.setFont(new Font("arial",Font.BOLD,20));
                    if(a == 1) {
                        g.drawString("> Easy", 10, 100);
                        g.drawString("Medium", 10, 150);
                        g.drawString("Hard", 10, 200);
                        if(b == 1) {
                            g.drawString("> 5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 2) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("> 10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 3) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("> 15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 4) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("> free play", 10, 450);
                        }
                    } else if(a == 2) {
                        g.drawString("Easy", 10, 100);
                        g.drawString("> Medium", 10, 150);
                        g.drawString("Hard", 10, 200);
                        g.drawString("10 point", 10, 300);
                        g.drawString("free play", 10, 350);
                        if(b == 1) {
                            g.drawString("> 5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 2) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("> 10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 3) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("> 15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 4) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("> free play", 10, 450);
                        }
                    } else if(a == 3) {
                        g.drawString("Easy", 10, 100);
                        g.drawString("Medium", 10, 150);
                        g.drawString("> Hard", 10, 200);
                        g.drawString("10 point", 10, 300);
                        g.drawString("free play", 10, 350);
                        if(b == 1) {
                            g.drawString("> 5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 2) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("> 10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 3) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("> 15 point", 10, 400);
                            g.drawString("free play", 10, 450);
                        } else if(b == 4) {
                            g.drawString("5 point", 10, 300);
                            g.drawString("10 point", 10, 350);
                            g.drawString("15 point", 10, 400);
                            g.drawString("> free play", 10, 450);
                        }
                    }
                    g.dispose();
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException ie) {
                        
                    }
                }
            }
        };
        tt.start();
        t = new Thread() {
            public void run() {
                while(true) {
                    if(a==1) {
                        if(ball.x < enemyPaddle.x+enemyPaddle.width/2) {
                            enemyPaddle.move_left();
                        }
                        if(ball.x > enemyPaddle.x+enemyPaddle.width/2) {
                            enemyPaddle.move_right();
                        }
                    } else if(a==2) {
                        if(ball.x < enemyPaddle.x+enemyPaddle.width/2) {
                            enemyPaddle.move_left_mid();
                        }
                        if(ball.x > enemyPaddle.x+enemyPaddle.width/2) {
                            enemyPaddle.move_right_mid();
                        }
                    } else if(a==3) {
                        if(ball.x < enemyPaddle.x+enemyPaddle.width/2) {
                            enemyPaddle.move_left();
                            enemyPaddle.move_left();
                        }
                        if(ball.x > enemyPaddle.x+enemyPaddle.width/2) {
                            enemyPaddle.move_right();
                            enemyPaddle.move_right();
                        }
                    }
                    if(vk.equals("left")) {
                        vk = "";
                        if(Math.abs(time - timet) < 2000) {
                            if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                                paddle.accel_left();
                                ball.move();
                            }
                        }
                    }
                    if(vk.equals("right")) {
                        vk = "";
                        if(Math.abs(time - timet) < 2000) {
                            if(ball.x >= paddle.x && ball.x <= paddle.x + paddle.width && ball.y >= paddle.y-10 && ball.y <= paddle.y+10 + 15) {
                                paddle.accel_right();
                                ball.move();
                            }
                        }
                    }
                    if(b == 4)
                        j.setTitle("pong. {Free Points} Human: " + lives + " v. Computer: " + lives2);
                    else
                        j.setTitle("pong. {Points} Human: " + lives + " v. Computer: " + lives2);
                    try {
                        if(b == 1) {
                            if(lives == 5) {
                                menu = true;
                                JOptionPane.showMessageDialog(null, "You win");
                                t.stop();
                            }
                            if(lives2 == 5) {
                                menu = true;
                                JOptionPane.showMessageDialog(null, "You lose");
                                t.stop();
                            }
                        }
                        if(b == 2) {
                            if(lives == 10) {
                                menu = true;
                                JOptionPane.showMessageDialog(null, "You win");
                                t.stop();
                            }
                            if(lives2 == 10) {
                                menu = true;
                                JOptionPane.showMessageDialog(null, "You lose");
                                t.stop();
                            }
                        }
                        if(b == 3) {
                            if(lives == 15) {
                                menu = true;
                                JOptionPane.showMessageDialog(null, "You win");
                                t.stop();
                            }
                            if(lives2 == 15) {
                                menu = true;
                                JOptionPane.showMessageDialog(null, "You lose");
                                t.stop();
                            }
                        }
                        if(b == 1 || b == 2 || b == 3) {
                            if(lives == 5 || lives2 == 5 || lives == 10 || lives2 == 10 || lives == 15 || lives2 == 15) {
                                tt = new Thread() {
                                    public void run() {
                                        while(true && menu) {
                                            if(!menu)
                                                break;
                                            Graphics g = p.getGraphics();
                                            try {
                                                if(bgimg == null)
                                                    bgimg = ImageIO.read(getClass().getResourceAsStream("bg.jpg"));
                                                g.drawImage(bgimg, 0, 0, 1000, 700, null);
                                            } catch(Exception e) {
                                                e.printStackTrace();
                                            }
                                            g.setFont(new Font("arial",Font.BOLD,40));g.drawString("PONG", 10, 50);g.setFont(new Font("arial",Font.BOLD,20));
                                            if(a == 1) {
                                                g.drawString("> Easy", 10, 100);
                                                g.drawString("Medium", 10, 150);
                                                g.drawString("Hard", 10, 200);
                                                if(b == 1) {
                                                    g.drawString("> 5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 2) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("> 10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 3) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("> 15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 4) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("> free play", 10, 450);
                                                }
                                            } else if(a == 2) {
                                                g.drawString("Easy", 10, 100);
                                                g.drawString("> Medium", 10, 150);
                                                g.drawString("Hard", 10, 200);
                                                if(b == 1) {
                                                    g.drawString("> 5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 2) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("> 10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 3) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("> 15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 4) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("> free play", 10, 450);
                                                }
                                            } else if(a == 3) {
                                                g.drawString("Easy", 10, 100);
                                                g.drawString("Medium", 10, 150);
                                                g.drawString("> Hard", 10, 200);
                                                if(b == 1) {
                                                    g.drawString("> 5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 2) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("> 10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 3) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("> 15 point", 10, 400);
                                                    g.drawString("free play", 10, 450);
                                                } else if(b == 4) {
                                                    g.drawString("5 point", 10, 300);
                                                    g.drawString("10 point", 10, 350);
                                                    g.drawString("15 point", 10, 400);
                                                    g.drawString("> free play", 10, 450);
                                                }
                                            }
                                            g.dispose();
                                            try {
                                                Thread.sleep(1000);
                                            } catch(InterruptedException ie) {

                                            }
                                        }
                                    }
                                };
                                tt.start();
                            }
                        }
                        Thread.sleep(50);
                        draw();
                        ball.move();
                        paddle.draw();
                        enemyPaddle.draw();
                        drawBall();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    
    Image ballimg = null;
    Image bgimg = null;
    Image paddleimg1 = null;
    Image paddleimg2 = null;
    
    private void drawBall() {
        Graphics g = p.getGraphics();
        g.setColor(Color.white);
        g.fillOval(ball.x,ball.y,20,20);
        g.dispose();
    }
    
    public void draw() {
        Graphics g = p.getGraphics();
        g.setColor(Color.PINK);
        g.fillRect(0,0,1000,700);
        g.dispose();
    }
    
    public static void main(String[] args) {
        new Pong();
    }
}
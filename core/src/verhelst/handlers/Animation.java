package verhelst.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Orion on 2/17/2015.
 */
public class Animation {
        private TextureRegion[] frames;
        private float time;
        private float delay;
        private int currentFrame;

        private int timesPlayed;
        private boolean repeat;

        public Animation() {}

        public Animation(TextureRegion[] frames) {
            this(frames, 1 / 12f);
        }

        public Animation(TextureRegion[] frames, float delay) {
            this.frames = frames;
            this.delay = delay;
            time = 0;
            currentFrame = 0;
        }

        public void setDelay(float f) { delay = f; }
        public void setCurrentFrame(int i) { if(i < frames.length) currentFrame = i; }
        public void setFrames(TextureRegion[] frames) {
            setFrames(frames, 1 / 12f);
        }
        public void setFrames(TextureRegion[] frames, float delay) {
            this.frames = frames;
            time = 0;
            currentFrame = 0;
            timesPlayed = 0;
            this.delay = delay;
        }

        public void update(float dt) {
            if(delay <= 0) return;
            time += dt;
            while(time >= delay) {
                step();
            }
        }

        private void step() {
            time -= delay;
            if(!repeat && timesPlayed > 0) {
                return;
            }
            currentFrame++;
            if(currentFrame == frames.length) {
                timesPlayed++;
                if(repeat)
                    currentFrame = 0;
                else
                    currentFrame--;

            }
        }


        public void setRepeat(boolean isRepeat){this.repeat = isRepeat;}
        public TextureRegion getFrame() { return frames[currentFrame]; }
        public int getTimesPlayed() { return timesPlayed; }
        public boolean hasPlayedOnce() { return timesPlayed > 0; }
        public boolean donePlaying() {return !repeat && timesPlayed > 0;}
}

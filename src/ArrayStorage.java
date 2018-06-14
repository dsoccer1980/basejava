import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int elementsCount = 0;

    public void clear() {
        Arrays.fill(storage, 0, elementsCount, null);
        elementsCount = 0;
    }

    public void save(Resume r) {
        if ((r != null) && getIndex(r.uuid) == -1) {
            storage[elementsCount++] = r;
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            return storage[index];
        }
        return null;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            System.arraycopy(storage, index + 1, storage, index, elementsCount - (index + 1));
            elementsCount--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, elementsCount);
    }

    public int size() {
        return elementsCount;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < elementsCount; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}

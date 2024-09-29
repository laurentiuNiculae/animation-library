package main

import (
	"bufio"
	"fmt"
	"os"
	"time"
)

func main() {
	// Create a channel to signal when reading is done or timeout happens
	done := make(chan bool)

	// Start a goroutine to read from stdin
	go func() {
		scanner := bufio.NewScanner(os.Stdin)
		for scanner.Scan() {
			input := scanner.Bytes()
			fmt.Println("Received:", input)
		}
		done <- true // Send signal when done reading
	}()

	// Set a timeout of 4 seconds
	select {
	case <-done:
		fmt.Println("Finished reading.")
	case <-time.After(4 * time.Second):
		fmt.Println("Timeout after 4 seconds.")
	}
}

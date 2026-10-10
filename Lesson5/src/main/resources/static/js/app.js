class PriceFeed {
    constructor() {
        this.isConnected = false;
        this.startTime = null;
        this.messageCount = 0;
        this.lastMessageTime = null;
        this.websocket = null;
        this.eventSource = null;
        this.longPollingActive = false;
        this.lastSeq = null;
        this.lostCount = 0;
        this.retryCount = 0;
        this.maxRetries = 3;
        
        this.initializeElements();
        this.attachEventListeners();
        this.addKeyboardShortcuts();
    }

    initializeElements() {
        // Get all required elements with error handling
        this.startButton = document.getElementById('startButton');
        this.stopButton = document.getElementById('stopButton');
        this.transportType = document.getElementById('transportType');
        this.status = document.getElementById('status');
        this.pricesContainer = document.getElementById('prices');
        this.messagesPerSecond = document.getElementById('messagesPerSecond');
        this.totalTime = document.getElementById('totalTime');
        this.totalMessages = document.getElementById('totalMessages');
        this.lostMessages = document.getElementById('lostMessages');

        // Validate all elements exist
        const requiredElements = [
            this.startButton, this.stopButton, this.transportType,
            this.status, this.pricesContainer, this.messagesPerSecond,
            this.totalTime, this.totalMessages, this.lostMessages
        ];

        if (requiredElements.some(el => !el)) {
            console.error('Missing required DOM elements');
            this.showError('Application initialization failed - missing elements');
            return;
        }
    }

    attachEventListeners() {
        this.startButton.addEventListener('click', () => this.start());
        this.stopButton.addEventListener('click', () => this.stop());
        
        // Add transport change handler
        this.transportType.addEventListener('change', () => {
            if (this.isConnected) {
                this.stop();
                setTimeout(() => this.start(), 100);
            }
        });
    }

    addKeyboardShortcuts() {
        document.addEventListener('keydown', (event) => {
            // Space bar to start/stop
            if (event.code === 'Space' && !event.target.matches('input, textarea, select')) {
                event.preventDefault();
                if (this.isConnected) {
                    this.stop();
                } else {
                    this.start();
                }
            }
            
            // Enter to toggle transport
            if (event.code === 'Enter' && !event.target.matches('input, textarea, select')) {
                event.preventDefault();
                const order = ['websocket', 'long-polling', 'sse'];
                const next = (order.indexOf(this.transportType.value) + 1) % order.length;
                this.transportType.value = order[next];
                if (this.isConnected) {
                    this.stop();
                    setTimeout(() => this.start(), 100);
                }
            }
        });
    }

    showError(message) {
        this.status.textContent = `Error: ${message}`;
        this.status.className = 'status disconnected';
    }

    showLoading(message = 'Connecting...') {
        this.status.textContent = message;
        this.status.className = 'status connecting';
    }

    async start() {
        if (this.isConnected) return;

        this.isConnected = true;
        this.startTime = Date.now();
        this.messageCount = 0;
        this.lastMessageTime = Date.now();
        this.retryCount = 0;
        this.lastSeq = null;
        this.lostCount = 0;

        // Clear existing data and show placeholder when starting
        this.pricesContainer.innerHTML = '<div class="price-item placeholder-row"><div>Connecting...</div><div></div><div></div><div></div><div></div><div></div></div>';

        this.updateUI();
        this.startStatsUpdate();

        const transport = this.transportType.value;
        try {
            if (transport === 'websocket') {
                await this.startWebSocket();
            } else if (transport === 'sse') {
                this.startSse();
            } else {
                await this.startLongPolling();
            }
        } catch (error) {
            console.error('Failed to start connection:', error);
            this.handleConnectionError(error);
        }
    }

    stop() {
        this.isConnected = false;
        
        if (this.websocket) {
            this.websocket.close();
            this.websocket = null;
        }

        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
        }
        
        this.longPollingActive = false;
        this.retryCount = 0;
        
        // Keep the data visible when stopping
        this.updateUI();
    }

    async startWebSocket() {
        try {
            this.showLoading('Establishing WebSocket connection...');

            // Determine protocol based on current page protocol
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${protocol}//${window.location.host}/prices/websocket`;
            
            console.log(`Connecting to WebSocket: ${wsUrl}`);
            
            // TODO 1: Create a WebSocket connection under this.websocket
            // HINT: WebSocket creates a persistent connection to the server
            
            this.websocket.onopen = () => {
                this.status.textContent = 'WebSocket Connection Established';
                this.status.className = 'status connected';
                this.retryCount = 0; // Reset retry count on successful connection
            };

            this.websocket.onmessage = (event) => {
                try {
                    // TODO 2: Handle incoming messages from the server (event.data)
                    // HINT: Parse the JSON message and call handleMessage() to process it
                } catch (error) {
                    console.error('Failed to parse WebSocket message:', error);
                }
            };

            this.websocket.onclose = (event) => {
                if (this.isConnected) {
                    this.status.textContent = 'Disconnected';
                    this.status.className = 'status disconnected';
                    this.isConnected = false;
                    this.updateUI();
                    
                    // Auto-retry on unexpected close
                    if (event.code !== 1000 && this.retryCount < this.maxRetries) {
                        this.retryCount++;
                        console.log(`WebSocket closed unexpectedly, retrying... (${this.retryCount}/${this.maxRetries})`);
                        setTimeout(() => {
                            if (!this.isConnected) {
                                this.start();
                            }
                        }, 2000 * this.retryCount);
                    }
                }
            };

            this.websocket.onerror = (error) => {
                console.error('WebSocket error:', error);
                this.handleConnectionError(error);
            };

        } catch (error) {
            console.error('Failed to start WebSocket:', error);
            this.handleConnectionError(error);
        }
    }

    async startLongPolling() {
        this.status.textContent = 'Long Polling Connection Active';
        this.status.className = 'status connected';
        this.longPollingActive = true;
        
        while (this.longPollingActive && this.isConnected) {
            try {
                // TODO 3: Make HTTP request to long polling endpoint
                // HINT: Use await fetch() to make a request that waits for data from the server
                // The endpoint is /prices/long-polling

                if (response.status === 204) {
                    // HTTP 204 (No Content) means no new data within timeout period
                    // Continue polling immediately - this is normal behavior
                    continue;
                } else if (response.ok) {
                    // TODO 4: Handle incoming messages from the server
                    // HINT: Use response.json() to parse the JSON response and call handleMessage() to process it

                    this.retryCount = 0; // Reset retry count on successful request
                } else {
                    throw new Error(`HTTP ${response.status}`);
                }
            } catch (error) {
                console.error('Long polling error:', error);
                this.handleConnectionError(error);
                break;
            }
        }
    }

    startSse() {
        this.status.textContent = 'SSE Connection Active';
        this.status.className = 'status connected';

        // TODO 5: Create the Server-Sent Events connection under this.eventSource
        // HINT: The browser API for SSE is EventSource. The endpoint is /prices/sse

        this.eventSource.onmessage = (event) => {
            try {
                // TODO 6: Handle incoming messages from the server (event.data)
                // HINT: Exactly the same as in WebSocket: parse the JSON and call handleMessage()
            } catch (error) {
                console.error('Failed to parse SSE message:', error);
            }
        };

        this.eventSource.onerror = (error) => {
            // EventSource reconnects automatically. We only stop if the user pressed Stop.
            console.warn('SSE error (the browser will try to reconnect):', error);
            if (!this.isConnected && this.eventSource) {
                this.eventSource.close();
            }
        };
    }

    handleConnectionError(error) {
        this.retryCount++;
        this.status.textContent = `Connection Error (${this.retryCount}/${this.maxRetries})`;
        this.status.className = 'status disconnected';
        
        if (this.retryCount < this.maxRetries) {
            console.log(`Retrying connection in ${this.retryCount * 2} seconds...`);
            setTimeout(() => {
                if (!this.isConnected) {
                    this.start();
                }
            }, this.retryCount * 2000);
        } else {
            this.isConnected = false;
            this.updateUI();
            this.showError('Max retries reached. Please try again.');
        }
    }

    handleMessage(priceMessage) {
        // Validate price message
        if (!this.isValidPriceMessage(priceMessage)) {
            console.warn('Invalid price message received:', priceMessage);
            return;
        }

        this.messageCount++;
        this.lastMessageTime = Date.now();

        // Detect lost messages: consecutive messages must have consecutive sequence numbers
        if (this.lastSeq !== null && priceMessage.seq > this.lastSeq + 1) {
            this.lostCount += priceMessage.seq - this.lastSeq - 1;
        }
        this.lastSeq = priceMessage.seq;
        
        // Remove placeholder row if it exists
        const placeholder = this.pricesContainer.querySelector('.placeholder-row');
        if (placeholder) {
            placeholder.remove();
        }
        
        // Add new price to the top
        const priceElement = this.createPriceElement(priceMessage);
        this.pricesContainer.insertBefore(priceElement, this.pricesContainer.firstChild);
        
        // Keep only last 50 messages
        while (this.pricesContainer.children.length > 50) {
            this.pricesContainer.removeChild(this.pricesContainer.lastChild);
        }
    }

    isValidPriceMessage(message) {
        return message &&
               typeof message.seq === 'number' &&
               typeof message.market === 'string' &&
               typeof message.product === 'string' &&
               typeof message.instrumentName === 'string' &&
               typeof message.level === 'number' &&
               typeof message.price === 'number' &&
               typeof message.volume === 'number';
    }

    createPriceElement(priceMessage) {
        const div = document.createElement('div');
        div.className = 'price-item';
        div.innerHTML = `
            <div>#${priceMessage.seq}</div>
            <div>${this.escapeHtml(priceMessage.product)}</div>
            <div>${this.escapeHtml(priceMessage.instrumentName)}</div>
            <div>${priceMessage.level}</div>
            <div class="price">$${priceMessage.price.toFixed(2)}</div>
            <div class="volume">${priceMessage.volume.toLocaleString()}</div>
        `;
        return div;
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    startStatsUpdate() {
        const updateStats = () => {
            if (!this.isConnected) return;

            const now = Date.now();
            const elapsed = Math.floor((now - this.startTime) / 1000);
            const minutes = Math.floor(elapsed / 60);
            const seconds = elapsed % 60;
            
            this.totalTime.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
            this.totalMessages.textContent = this.messageCount.toLocaleString();
            this.lostMessages.textContent = this.lostCount.toLocaleString();

            // Calculate messages per second (last 5 seconds)
            if (this.lastMessageTime && now - this.lastMessageTime < 5000) {
                const recentMessages = this.messageCount;
                const rate = recentMessages / (elapsed || 1);
                this.messagesPerSecond.textContent = rate.toFixed(1);
            }

            setTimeout(updateStats, 1000);
        };
        updateStats();
    }

    updateUI() {
        this.startButton.disabled = this.isConnected;
        this.stopButton.disabled = !this.isConnected;
        
        if (!this.isConnected) {
            this.status.textContent = 'Disconnected';
            this.status.className = 'status disconnected';
        }
    }
}

// Initialize the application when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    try {
        new PriceFeed();
    } catch (error) {
        console.error('Failed to initialize PriceFeed:', error);
        document.body.innerHTML = '<div style="text-align: center; padding: 50px; color: red;">Failed to load application. Please refresh the page.</div>';
    }
});
